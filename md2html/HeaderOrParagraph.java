package md2html;

abstract public class HeaderOrParagraph {
    String markup;
    int level;

    public HeaderOrParagraph(String markup, int level) {
        this.markup = markup;
        this.level = level;
    }

    public HeaderOrParagraph(String markup) {
        this.markup = markup;
    }

    abstract String getTag();

    String toHtml() {
        Text text = new Text(markup);
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(getTag()).append(">").append(text.toHtml()).append("</").append(getTag()).append(">");
        return sb.toString();
    }
}
