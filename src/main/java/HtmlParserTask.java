import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.RecursiveTask;

public class HtmlParserTask extends RecursiveTask<Set<String>> {
    private static final Logger LOGGER = LogManager.getLogger(HtmlParserTask.class);

    private final String parentLink;
    private final String domainName;
    private static final Set<String> commonList = new HashSet<>();
    private final Set<String> links;
    private final List<HtmlParserTask> tasks;

    public HtmlParserTask(String parentLink, String domainName) {
        this.parentLink = parentLink;
        this.domainName = domainName;
        links = new TreeSet<>();
        tasks = new ArrayList<>();
    }

    @Override
    protected Set<String> compute() {
        LOGGER.info("Парсим HTML-код страницы " + parentLink);
        try {
            collectLinksFromHtml();
        } catch (IOException | InterruptedException ex) {
            LOGGER.fatal("Поймано исключение при работе с ссылкой:\n" + parentLink +
                    "\nОшибка: " + ex.getLocalizedMessage());
        }
        addResultsFromTask();
        return links;
    }

    private void collectLinksFromHtml() throws InterruptedException, IOException {
        Document document = Jsoup.connect(parentLink).get();
        Thread.sleep(150);

        Elements elements = document.select("a");
        for (Element element : elements) {
            analyzeLink(element.attr("abs:href"));
        }
    }

    private void analyzeLink(String linkToAnalyze) {
        if (metLinkForTheFirstTime(linkToAnalyze) && linkIsSuitableForMap(linkToAnalyze)) {
            if (linkToAnalyze.contains("?") || linkToAnalyze.contains("&")) {
                linkToAnalyze = clearLink(linkToAnalyze);
                if (!metLinkForTheFirstTime(linkToAnalyze)) {
                    return;
                }
            }
            addLinkToSets(linkToAnalyze);
            if (!linkToFile(linkToAnalyze)) {
                createNewTask(linkToAnalyze);
            }
        }
    }

    private boolean metLinkForTheFirstTime(String link) {
        return !commonList.contains(link);
    }

    private boolean linkIsSuitableForMap(String link) {
        return linkIsChild(link) && linkIsNotToInternalElement(link);
    }

    private boolean linkIsChild(String link) {
        return link.startsWith(domainName);
    }

    private boolean linkIsNotToInternalElement(String link) {
        return !link.contains("#");
    }

    private String clearLink(String link) {
        if (link.contains("?")) {
            return link.split("\\?")[0];
        } else {
            return link.split("&")[0];
        }
    }

    private void addLinkToSets(String link) {
        commonList.add(link);
        links.add(link);
    }

    private boolean linkToFile(String link) {
        return link.endsWith(".pdf") || link.endsWith(".png");
    }

    private void createNewTask(String link) {
        HtmlParserTask task = new HtmlParserTask(link, domainName);
        task.fork();
        tasks.add(task);
    }

    private void addResultsFromTask() {
        for (HtmlParserTask task : tasks) {
            links.addAll(task.join());
        }
    }
}