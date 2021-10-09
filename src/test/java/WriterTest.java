import core.Link;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тестирование записи в файл")

public class WriterTest {
    private static final String PARENT_LINK = "https://skillbox.ru";
    private static final String TEST_MAP = "src/test/resources/mapTest.txt";
    public static final String RESULT_MAP = "src/main/resources/map.txt";

    private final Set<Link> links = new TreeSet<>();

    @BeforeEach
    public void setUp() {
        links.add(new Link(PARENT_LINK));
        links.add(new Link(PARENT_LINK + "/media/"));
        links.add(new Link(PARENT_LINK + "/courses/"));
        links.add(new Link(PARENT_LINK + "/oferta.pdf"));
        links.add(new Link(PARENT_LINK + "/courses/zifrovai-transformazia/"));
        links.add(new Link(PARENT_LINK + "/course/2d-graphics/"));
        links.add(new Link(PARENT_LINK + "/media/design/figma-images/"));
    }

    @Test
    @DisplayName("Проверяем, правильно ли создан файл с картой")
    public void testCreateMapFromSet() throws IOException {
        Writer writer = new Writer(links);
        writer.writeLinksToMap();
        String expected = new String(Files.readAllBytes(Paths.get(TEST_MAP)));
        String actual = new String(Files.readAllBytes(Paths.get(RESULT_MAP)));
        assertEquals(expected, actual);
    }
}
