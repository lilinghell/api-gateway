package com.hell.action.test.cases;

import com.hell.common.Dictionary;
import com.hell.common.ServiceUtils;
import com.hell.dao.STestCasesDao;
import com.hell.dao.STestGroupDao;
import com.hell.db.table.provider.STestCases;
import com.hell.db.table.provider.STestGroup;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.QryTestCasesRequest;
import com.hell.dto.response.test.TestCasesResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "test")
public class QryTestCasesAction implements BaseAction<QryTestCasesRequest, TestCasesResponse> {
    private STestCasesDao testCasesDao;
    private STestGroupDao testGroupDao;

    @GetMapping(path = "/api/t1/test/qryTestCases")
    @ApiOperation("测试用例查询")
    @Override
    public TestCasesResponse execute(HttpServletRequest httpServletRequest, QryTestCasesRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        List<STestCases> cases = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(request.getTestGroupSeq())) {
            if (request.getTestGroupSeq().compareTo(-1) == 0) {
                //查询所有的用例数,除了基础用例外...
                cases = testCasesDao.findByCaseTypeNotAndEntSeqAndAppSeq(Dictionary.CASE_TYPE_2, entSeq,
                        request.getAppSeq(),
                        Sort.by(Sort.Direction.DESC, "apiPath"));
            } else if (request.getTestGroupSeq().compareTo(-2) == 0) {
                //基础用例 每次运行其它用例时，只运行一次，如登陆接口保存cookie,
                cases = testCasesDao.findByCaseTypeAndEntSeqAndAppSeq(Dictionary.CASE_TYPE_2, entSeq,
                        request.getAppSeq());
            } else {
                List<Integer> groupSeqList = new ArrayList<>();
                groupSeqList.add(request.getTestGroupSeq());
                List<STestGroup> list = testGroupDao.findByParentSeqAndEntSeq(request.getTestGroupSeq(), entSeq);
                list.forEach(testGroup -> {
                    groupSeqList.add(testGroup.getSeq());
                });

                cases = testCasesDao.findByTestGroupSeqInAndEntSeq(groupSeqList, entSeq,
                        Sort.by(Sort.Direction.DESC, "seq"));
            }
        } else if (ObjectUtils.isNotEmpty(request.getCaseSeq())) {
            STestCases testCases = testCasesDao.findBySeqAndEntSeq(request.getCaseSeq(), entSeq);
            cases.add(testCases);
        } else {
            cases = testCasesDao.findByEntSeqAndAppSeq(entSeq,
                    request.getAppSeq(),Sort.by(Sort.Direction.DESC, "seq"));
        }

        TestCasesResponse re = new TestCasesResponse();
        re.setCases(cases);
        return re;
    }

    @Autowired
    public void setTestCasesDao(STestCasesDao testCasesDao) {
        this.testCasesDao = testCasesDao;
    }

    @Autowired
    public void setTestGroupDao(STestGroupDao testGroupDao) {
        this.testGroupDao = testGroupDao;
    }
}
