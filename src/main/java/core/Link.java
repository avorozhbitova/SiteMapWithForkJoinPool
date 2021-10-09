package core;

public class Link implements Comparable<Link> {
    private final String url;
    private final int nestingLevel;

    public Link(String url) {
        this.url = url;
        nestingLevel = calculateNestingLevel();
    }

    private int calculateNestingLevel() {
        int countOfSlash = -2;
        for (int i = 0; i < url.length(); i++) {
            if (url.charAt(i) == '/' && i != url.length() - 1) {
                countOfSlash++;
            }
        }
        return countOfSlash;
    }

    public String getUrl() {
        return url;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    @Override
    public String toString() {
        return "Ссылка - " + url + ", вложенность - " + nestingLevel;
    }

    @Override
    public int compareTo(Link linkToCompare) {
        return url.compareTo(linkToCompare.getUrl());
    }
}