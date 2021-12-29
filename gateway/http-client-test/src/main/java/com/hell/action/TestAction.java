package com.hell.action;

import com.hell.service.TestService;
import com.hell.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestAction {

    TestService testService;

    @GetMapping(path = "/")
    public String root() throws Exception {
        //String response = testService.qyrService();

        // String re = HttpClientUtils.doGet("/api/common/token", null);
        return "liling";
    }

    @RequestMapping(path = "/api/qry", headers = "X-API-VERSION=1")
    public String qry(HttpSession session) throws Exception {
        System.out.println(session.getId());
        Map m = new HashMap();
        m.put("RspCode", "000000");
        m.put("RspMsg", "success");
        Map m2 = new HashMap();
        m2.put("name", "liling");
        m2.put("id", "2");
        m2.put("status", "01");
        m.put("ResultData", m2);
        return JsonUtils.objectToJson(m);
    }

    @GetMapping(path = "/api/qry", headers = "X-API-VERSION=2")
    public String qry2(HttpSession session) throws Exception {
        System.out.println(session.getId());
        return "liling2";
    }

    @GetMapping(path = "/api/qry")
    public String qry3() throws Exception {
        return "liling3";
    }

    @Autowired
    public void setTestService(TestService testService) {
        this.testService = testService;
    }
}
