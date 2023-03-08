package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Md2Html {

    public static void main(String[] args) {
        Header header;
        HtmlParagraph paragraph;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            int level = 0;
            String line;
            boolean isParagraph = false;
            do {
                line = reader.readLine();
                if (line == null || line.equals("")) {
                    if (level > 0 && sb.length() > level) {
                        header = new Header(sb.substring(level + 1), level);
                        writer.write(header.toHtml());
                        writer.newLine();
                    } else if (level == 0 && sb.length() > 0) {
                        paragraph = new HtmlParagraph(sb.toString());
                        writer.write(paragraph.toHtml());
                        writer.newLine();
                    }
                    level = 0;
                    isParagraph = false;
                    sb = new StringBuilder();
                } else {
                    sb.append(line).append("\n");
                    if (!isParagraph && line.startsWith("#")) {
                        while (line.charAt(level) == '#') {
                            level++;
                        }
                        if (!Character.isWhitespace(line.charAt(level))) {
                            level = 0;
                        }
                        isParagraph = true;
                    }
                }
            } while (line != null);
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException was raised" + e.getMessage());
        }
    }
}
