public class Main {
    private static final String SKILLBOX_URL = "https://skillbox.ru/";
    private static final String LENTA_URL = "https://lenta.ru/";

    public static void main(String[] args) {

        Parser parser = new Parser(SKILLBOX_URL);
        parser.parseHtml();

        Writer writer = new Writer(parser.getLinks());
        writer.writeLinksToMap();
    }
}