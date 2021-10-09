import core.Link;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Writer {
    private static final String MAP_FILE = "src/main/resources/map.txt";

    private final Set<Link> links;
    private final List<String> listOfLinks;

    public Writer(Set<Link> links) {
        this.links = links;
        listOfLinks = new ArrayList<>();
    }

    public void writeLinksToMap() {
        convertSetToList();
        try {
            Files.write(Path.of(MAP_FILE), listOfLinks);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void convertSetToList() {
        for (Link link : links) {
            String linkWithTabs = multiplyTabs(link.getNestingLevel()) + link.getUrl();
            listOfLinks.add(linkWithTabs);
        }
    }

    private String multiplyTabs(int nestingLevel) {
        return "\t".repeat(nestingLevel);
    }
}