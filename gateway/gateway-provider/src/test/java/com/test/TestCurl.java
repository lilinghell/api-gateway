package com.test;


import org.springframework.util.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCurl {
    public static void main(String[] args) {
        System.out.println(DigestUtils.md5DigestAsHex("abx".getBytes()));
        String[] curlStr = {"curl", "-X", "GET", "http://localhost:9080/api/qry",
                "-H", "accept: application/json",
                "-H", "Content-Type: application/json",
                "-d", "{\n" +
                "  \"name\": \"string\",\n" +
                "  \"id\": 1,\n" +
                "  \"status\": \"string\"\n" +
                "}"
        };//必须分开写，不能有空格df

        String a = "curl -X 'POST' \n" +
                " 'https://xxx.com.cn/demo/qry' \n" +
                " -H 'accept: application/json' \n" +
                " -H 'Content-Type: application/json' \n" +
                " -d '{\n" +
                "  \"name\": \"string\",\n" +
                "  \"id\": 1,\n" +
                "  \"status\": \"string\"\n" +
                "}'";

        String[] fef = {"curl", "-c", "cookies.txt", "http://localhost:9080/api/qry"};

        //System.out.println(execCurl(fef));
        String[] fef2 = {"curl", "-b", "cookies.txt", "http://localhost:9080/api/qry2"};
        //System.out.println(execCurl(fef2));

        String[] demo = {"curl", "-c", "cookies.txt","-b", "cookies.txt", "-X", "POST", "http://localhost:8081/api/common/login",
                "-H", "accept: application/json",
                "-H", "Content-Type: application/json",
                "-d", "{\"loginId\":\"sys\",\"loginPassword\":\"000000\"}"
        };
        System.out.println(execCurl(demo));
        String[] demo2 = {"curl", "-b", "cookies.txt", "-X", "GET", "http://localhost:8081/api/t1/test/qryTestCases?testGroupSeq=857",
                "-H", "accept: application/json",
                "-H", "Content-Type: application/json"
        };
       // System.out.println(execCurl(demo2));
//        curl -b cookies.txt -c cookies.txt http://example.com

        //e();
//        d();
//        f();
//        System.out.println(findStr("nihao"));
    }

    public static List<String> findStr(String str) {
        //找出单引号号里面的内容
        Pattern p1 = Pattern.compile("\'(.*?)\'");

        Matcher m = p1.matcher(str);
        StringBuilder stringBuilder = new StringBuilder();

        List<String> list = new ArrayList<String>();
        while (m.find()) {
            System.out.println(m.group().trim().replace("\'", "") + "");
            list.add(m.group().trim().replace("\'", "") + "");
        }
        return list;
    }

    public static void f() {
        String str = "http://localhost:9080/api/qry?userName=@{UserName}";
        String p1 = "\\@\\{[^\\{\\}]{1,}\\}";
        Pattern p = Pattern.compile(p1);
        Matcher m = p.matcher(str);
        while (m.find()) {
            String key = m.group();
            String qryKey = key.substring(2, key.length() - 1);
            System.out.println(key + ":" + qryKey);
            str = str.replace(key, "nihao");
            System.out.println(str);
        }
    }


    public static void d() {
        String str = "curl -X 'POST'  'https://xxx.com.cn/demo/qry'  -H 'accept: application/json'  -H 'Content-Type: application/json'  -d '{  \"name\": \"string\",  \"id\": 1,  \"status\": \"string\"}'";
        //System.out.println(str.substring(str.indexOf("https://xxx.com.cn/demo/qry"), str.length()));
        System.out.println(str.substring(str.indexOf("-") + 1, str.length()));
        String[] k = str.split(" -");
        Pattern p1 = Pattern.compile("\'(.*?)\'");

        Matcher m = p1.matcher(str);
        StringBuilder stringBuilder = new StringBuilder();

        ArrayList<String> list = new ArrayList<String>();
        while (m.find()) {
            list.add(m.group().trim().replace("\'", "") + " ");
            // stringBuilder.append(m.group().trim().replace("\"","")+" ");
        }
        // System.out.println(stringBuilder.toString());
        System.out.println(list.toString());
    }

    public static void e() {
        ExecutorService service = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 20; i++) {
            System.out.println("创建线程" + i);
            int finalI = i;
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    System.out.println("启动:" + finalI);
                }
            };
            service.execute(run);
        }
    }

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
            System.out.print("error");
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
        return null;
    }
}
