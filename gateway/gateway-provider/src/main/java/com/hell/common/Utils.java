package com.hell.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
    private final static Logger log = LoggerFactory.getLogger(Utils.class);

    public static String execCurl(String[] curlStr) {

        BufferedReader reader = null;
        InputStreamReader input = null;
        try {
            ProcessBuilder process = new ProcessBuilder(curlStr);
            Process p;
            p = process.start();
            input = new InputStreamReader(p.getInputStream());
            reader = new BufferedReader(input);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            return builder.toString();
        } catch (IOException e) {
            log.error("exec curl error");
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "exec curl error";
    }
}
