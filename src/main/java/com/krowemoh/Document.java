package com.krowemoh;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


class Document {
    private String total, content, nextPageURL;
    private boolean isLast;

    public Document() {
        isLast = false;
    }
    public Document(String tt) {
        total = tt;
        /*
         * The regex formula for the doc
         * This consist of 6 parts
         * 1. before book name
         * 2. book name
         * 3. before content
         * 4. content
         * 5. after content
         * 6. next chapter
         */
        Pattern pattern = Pattern.compile("(<div class=\"bookname\">\\s+<h1>\\s+)([^<]+)([\\s\\S]+)(<div id=\"content\">[\\s\\S]+</div>\\s+<script>bdshare)([\\s\\S]+)(\"[/_\\d]+(.html)?\">[^<]+</a>[^&<]+<)");
        Matcher m = pattern.matcher(tt);
        if (m.find()) {
            String xxContent = m.group(4);
            String xxNextURL = m.group(6);
            nextPageURL = xxNextURL.substring(1, xxNextURL.lastIndexOf('\"'));
            // Replace all the non-sense blank line
            content = m.group(2)+"\n\n"+xxContent.replace("[&nbsp;&nbsp;]", "").replace("&nbsp;", "").replace("<br />", "").replaceAll("</div>\\s+<script>bdshare", "").substring(18) + "\n\n\n\n";
            isLast = nextPageURL.equals("/0_3/");
        } else {
            System.out.println("Error: Regex Error!");
        }
    }
    public String getNextPage() {
        return nextPageURL;
    }
    public String getTotal() {
        return total;
    }
    public String getContent() {
        return content;
    }
    public boolean last() {
        return isLast;
    }
}
