package md2html;

public class HtmlParagraph extends HeaderOrParagraph {

    public HtmlParagraph(String markup) {
        super(markup);
    }

    @Override
    String getTag() {
        return "p";
    }
}
