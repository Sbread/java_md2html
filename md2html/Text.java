package md2html;

import java.util.Map;
import java.util.Stack;

public class Text {
    String markup;
    StringBuilder sb;
    Stack<String> tags;

    private static final Map<String, String> htmlTags = Map.of(
            "**", "strong",
            "__", "strong",
            "--", "s",
            "<<", "ins",
            ">>", "ins",
            "{{", "del",
            "}}", "del"
    );

    private static final Map<String, String> singleHtmlTags = Map.of(
            "*", "em",
            "_", "em",
            "`", "code"
    );

    private static final Map<String, String> specialTags = Map.of(
            ">", "&gt;",
            "<", "&lt;",
            "&", "&amp;"
    );

    public Text(String markup) {
        this.markup = markup;
        sb = new StringBuilder();
        tags = new Stack<>();
    }

    int parseTag(String tag, boolean prevCharIsWS) {
        String singleTag = tag.substring(0, 1);
        if (htmlTags.get(tag) != null) {
            return 2; //doubleTag
        } else if (specialTags.get(singleTag) != null) {
            return 0; //specialTag
        } else if (singleHtmlTags.get(singleTag) != null
                && tag.length() > 1 && (!prevCharIsWS || !Character.isWhitespace(tag.charAt(1)))) {
            return 1; //singleTag
        }
        return -1; //not tag
    }

    void appendTag(String tag) {
        if (!tags.isEmpty() && tag.equals(tags.peek())) {
            sb.append("</").append(tag).append(">");
            tags.pop();
        } else {
            sb.append("<").append(tag).append(">");
            tags.push(tag);
        }
    }

    String toHtml() {
        boolean prevCharIsWS;
        for (int i = 0; i < markup.length(); i++) {
            if (markup.charAt(i) == '\\') {
                if (i + 1 < markup.length()) {
                    String tag = markup.substring(i + 1, i + 2);
                    sb.append(specialTags.getOrDefault(tag, tag));
                    i++;
                    continue;
                }
            }
            prevCharIsWS = false;
            if (i > 0 && Character.isWhitespace(markup.charAt(i - 1))) {
                prevCharIsWS = true;
            }
            int preTag = parseTag(markup.substring(i, Math.min(i + 2, markup.length())), prevCharIsWS);
            String tag;
            switch (preTag) {
                case 2:
                    tag = htmlTags.get(markup.substring(i, i + 2));
                    i++;
                    appendTag(tag);
                    break;
                case 1:
                    tag = singleHtmlTags.get(markup.substring(i, i + 1));
                    appendTag(tag);
                    break;
                case 0:
                    tag = specialTags.get(markup.substring(i, i + 1));
                    sb.append(tag);
                    break;
                case -1:
                    sb.append(markup.charAt(i));
                    break;
            }
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
