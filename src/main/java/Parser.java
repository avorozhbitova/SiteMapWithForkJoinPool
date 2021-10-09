import core.Link;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinPool;

public class Parser {
    private final String parentLink;
    private final Set<String> plainLinks;
    private final Set<Link> links;

    public Parser(String parentLink) {
        this.parentLink = parentLink;
        plainLinks = new TreeSet<>();
        links = new TreeSet<>();
    }

    public void parseHtml() {
        plainLinks.addAll(new ForkJoinPool().invoke(new HtmlParserTask(parentLink, parentLink)));
        convertStringsToLinks();
    }

    private void convertStringsToLinks() {
        for (String link : plainLinks) {
            links.add(new Link(link));
        }
    }

    public void printList() {
        links.forEach(System.out::println);
        System.out.println("Размер списка = " + links.size());
    }

    public Set<Link> getLinks() {
        return links;
    }
}