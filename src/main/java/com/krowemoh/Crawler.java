package com.krowemoh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread;
import java.lang.InterruptedException;
import org.apache.commons.cli.*;


public class Crawler {
    public static void main(String[] args) {
        /*
         * Process Args
         * -h -help     show the help info
         * -o -output   the output file name or path(default novel.txt)
         * -a -append   the start relative url(like /0_3/3361.html)
         */
        boolean outputFlag = false, appendFlag = false;
        String outputArgVal = null, appendArgVal = null;
        Options options = new Options();
        options.addOption("h", "help", false, "Show the help info.")
                .addOption("o", "output", false, "the output file name or path(default novel.txt)").addOption("a", "append", false, "the start relative url(like /0_3/3361.html)");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Crawler", options);
            return;
        }
        if (cmd.hasOption("o")) {
            outputFlag = true;
            outputArgVal = cmd.getOptionValue("o");
        }
        if (cmd.hasOption("a")) {
            appendFlag = true;
            appendArgVal = cmd.getOptionValue("a");
        }

        // ---- End Args Processing --- //

        File file = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        String site = "http://www.biqige.com";
        // String crtChap = "/0_3/2550.html";
        String crtChap = appendFlag?appendArgVal:"/0_3/470.html";
        Document doc = new Document();
        // System.out.print(res);
        try {
            file = new File(outputFlag?outputArgVal:"novel.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile(), appendFlag);
            bw = new BufferedWriter(fw);
            long startTime = 0, endTime = 0;

            int cnt = 1;
            while (!doc.last()) {
                endTime = System.currentTimeMillis();
                System.out.println("Query(" + cnt + ") For : " + site + crtChap + "\nWith Time gap "+ (endTime-startTime));
                cnt++;
                startTime = endTime;
                doc = HttpUtils.getByUri(site + crtChap);
                bw.write(doc.getContent());
                crtChap = doc.getNextPage();
                Thread.sleep(200);
            }
        } catch (InterruptedException | IOException ie) {
            ie.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
    }
}
