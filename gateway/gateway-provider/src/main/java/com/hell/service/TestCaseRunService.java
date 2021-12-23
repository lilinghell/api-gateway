package com.hell.service;

import com.hell.common.Dictionary;
import com.hell.common.Utils;
import com.hell.dao.SParameterDao;
import com.hell.dao.STestCasesDao;
import com.hell.dao.STestRunDao;
import com.hell.db.table.provider.SParameter;
import com.hell.db.table.provider.STestCases;
import com.hell.db.table.provider.STestRun;
import com.hell.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TestCaseRunService implements Serializable {

    private STestCasesDao testCasesDao;
    private STestRunDao testRunDao;
    private SParameterDao parameterDao;
    private MessageService messageService;
    ExecutorService service = Executors.newFixedThreadPool(5);


    public void testRun(List<Integer> caseSeq, Integer entSeq, Integer appSeq) {
        //#############
        List<STestCases> testCasesList = testCasesDao.findBySeqInAndEntSeq(caseSeq, entSeq);
        Map<String, String> staticParameterMap = new ConcurrentHashMap<>();//常量
        List<SParameter> parameterList = parameterDao.findByAppSeqAndTypeAndEntSeq(appSeq, "0", entSeq);//常量
        parameterList.forEach(para -> {
            staticParameterMap.put(para.getEnName(), para.getValue());
        });
        for (int i = 0; i < testCasesList.size(); i++) {
            int finalI = i;
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    //###################
                    long startTime = System.currentTimeMillis();
                    STestCases testCase = testCasesList.get(finalI);
                    String status = Dictionary.CASE_STATUS_3;//执行中
                    String response = "";
                    long execTime = 0L;
                    try {
                        //更新执行中状态
                        updateTestCaseStatus(testCase, status);
                        //run
                        //###################
                        response = testCaseRun(testCase);
                        status = Dictionary.CASE_STATUS_0;//执行成功
                        execTime = System.currentTimeMillis() - startTime;
                        //运行结果校验
                        status = checkResult(testCase, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        status = Dictionary.CASE_STATUS_1;//执行失败
                        response = "test run error:" + e.getMessage();
                    } finally {
                        //记录测试流水，更新状态
                        initTestCaseRunJnl(testCase, response, status, execTime);
                        if (finalI == testCasesList.size() - 1) {
                            //通知结果
                            Message message = new Message();
                            message.setType("1");
                            message.setVal("testRunOk");
                            messageService.notifyMsg(message);
                        }
                    }
                }

                private Object getTargetValue(String targetPara, String response) throws JsonProcessingException {
                    Object targetVal = null;
                    if (!response.equals("")) {
                        HashMap re = com.hell.core.common.utils.Utils.jsonToPojo(response, HashMap.class);
                        String keyRoot = targetPara;
                        keyRoot = StringUtils.strip(keyRoot);
                        String[] key = keyRoot.split("\\.");
                        for (int i = 0; i < key.length; i++) {
                            if (re.get(key[i]) instanceof Map) {
                                re = (HashMap) re.get(key[i]);
                            } else if (re.get(key[i]) instanceof List) {
                                //todo
                                targetVal = re.get(key[i]);
                            } else {
                                targetVal = re.get(key[i]);
                            }
                        }
                    }
                    return targetVal;
                }

                private String checkResult(STestCases testCase, String response) throws JsonProcessingException {
                    String status = Dictionary.CASE_STATUS_5;//验证失败
                    String script = testCase.getAssertScript();
                    script = StringUtils.removeStart(script, "$res.body.");
                    String[] ok = script.split("==");
                    if (ok.length > 1) {
                        Object chekVal = getTargetValue(ok[0], response);
                        if (String.valueOf(StringUtils.strip(ok[1])).equals(String.valueOf(chekVal))) {
                            status = Dictionary.CASE_STATUS_4;//验证成功
                        }
                    }
                    return status;
                }

                private void initTestCaseRunJnl(STestCases testCase, String response, String status, long execTime) {
                    updateTestCaseStatus(testCase, status);

                    STestRun testRun = testRunDao.findByCaseSeqAndEntSeq(testCase.getSeq(), entSeq);
                    if (ObjectUtils.isEmpty(testRun)) {
                        testRun = new STestRun();
                    }
                    testRun.setRunType(Dictionary.RUN_TYPE_0);
                    testRun.setCaseSeq(testCase.getSeq());
                    testRun.setEntSeq(testCase.getEntSeq());
                    testRun.setRequestParameter(testCase.getParameter());
                    testRun.setResponseResult(response);
                    testRun.setStatus(status);
                    testRun.setExecTime(String.valueOf(execTime));
                    testRun.setAppSeq(testCase.getAppSeq());
                    testRunDao.save(testRun);
                }

                private String testCaseRun(STestCases testCase) throws JsonProcessingException {
                    String parameter = testCase.getParameter();
                    String[] curlStr = assembleCurl(parameter, testCase);
                    String response = Utils.execCurl(curlStr);
                    return response;
                }

                private void updateTestCaseStatus(STestCases testCase, String status) {
                    testCase.setStatus(status);
                    testCasesDao.save(testCase);
                }

                private String[] assembleCurl(String parameter, STestCases testCase) throws JsonProcessingException {
                    List<String> list = new ArrayList<String>();
                    list.add("curl");
                    list.add("-c");
                    list.add("run_cookies_" + testCase.getAppSeq() + ".txt");
                    list.add("-b");
                    list.add("run_cookies_" + testCase.getAppSeq() + ".txt");
                    list.add("-X");

                    String str = parameter.replaceAll("\r|\n", "");
                    str = StringUtils.strip(str);

                    List<String> l = findStr(str);
                    list.add(StringUtils.strip(l.get(0)));//获取method
                    String url = l.get(1);
                    url = assembleParameter(url);
                    //###################
                    list.add(StringUtils.strip(url));//获取url
                    str = str.substring(str.indexOf("-") + 1, str.length());
                    str = str.substring(str.indexOf("-") + 1, str.length());
                    String[] k = str.split(" -");
                    for (int i = 0; i < k.length; i++) {
                        String o = k[i];
                        String b = o.substring(0, 1);
                        list.add(StringUtils.strip("-" + b));
                        String j = o.substring(1, o.length());
                        j = StringUtils.strip(j, " ");
                        j = StringUtils.strip(j, "'");
                        j = assembleParameter(j);
                        if (StringUtils.lowerCase(b).equals("d")) {
                            String os = System.getProperties().getProperty("os.name");
                            if (StringUtils.lowerCase(os).indexOf("windows") != -1) {
                                //windows 系统 添加转译服
                                j = j.replaceAll("\"", "\\\\\"");
                            }
                        }
                        list.add(StringUtils.strip(j));
                    }
                    return list.toArray(new String[list.size()]);
                }

                private String assembleParameter(String parameter) throws JsonProcessingException {
                    //###################
                    String str = StringUtils.strip(parameter);
                    String p1 = "\\@\\{[^\\{\\}]{1,}\\}";
                    Pattern p = Pattern.compile(p1);
                    Matcher m = p.matcher(str);
                    while (m.find()) {
                        String key = m.group();
                        String qryKey = key.substring(2, key.length() - 1);
                        String value = staticParameterMap.get(qryKey);
                        str = str.replace(key, value);
                    }
                    String p2 = "\\$\\{[^\\{\\}]{1,}\\}";
                    Pattern pp = Pattern.compile(p2);
                    Matcher m2 = pp.matcher(str);
                    while (m2.find()) {
                        String key = m2.group();
                        String qryKey = key.substring(2, key.length() - 1);
                        String[] dKey = qryKey.split("\\.");
                        //${32.res.body.data.userId}
                        STestCases c = testCasesDao.findByUniKeyAndEntSeq(Integer.valueOf(dKey[0]), entSeq);
                        String response = testCaseRun(c);
                        Object v = "";
                        if (dKey.length > 2) {
                            String targetPara = "";
                            for (int i = 3; i < dKey.length; i++) {
                                targetPara = targetPara + dKey[i] + ".";
                            }
                            targetPara = targetPara.substring(0, targetPara.length() - 1);
                            v = getTargetValue(targetPara, response);
                        }
                        str = str.replace(key, String.valueOf(v));
                    }
                    return str;
                }

                private List<String> findStr(String str) {
                    //找出单引号号里面的内容
                    Pattern p1 = Pattern.compile("\'(.*?)\'");

                    Matcher m = p1.matcher(str);
                    StringBuilder stringBuilder = new StringBuilder();

                    List<String> list = new ArrayList<String>();
                    while (m.find()) {
                        list.add(m.group().trim().replace("\'", "") + "");
                    }
                    return list;
                }
            };
            service.execute(run);
        }

    }


    @Autowired
    public void setTestCasesDao(STestCasesDao testCasesDao) {
        this.testCasesDao = testCasesDao;
    }

    @Autowired
    public void setTestRunDao(STestRunDao testRunDao) {
        this.testRunDao = testRunDao;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setParameterDao(SParameterDao parameterDao) {
        this.parameterDao = parameterDao;
    }
}
