package md2html;

public class Header extends HeaderOrParagraph {
    public Header(String markup, int level) {
        super(markup, level);
    }

    @Override
    String getTag() {
        return "h" + level;
    }
}
