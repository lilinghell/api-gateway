package com.hell.dao;

import com.hell.db.table.provider.STestCases;
import com.hell.db.table.provider.STestPlanCases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface STestPlanCasesDao extends JpaRepository<STestPlanCases, Integer> {

    @Query(value = "select t from STestCases t where t.seq in (" +
            "select p.testCaseSeq from STestPlanCases p where p.testPlanSeq=?2 and p.entSeq=?1 and p.appSeq=?3)")
    List<STestCases> qryTestPlanCases(Integer entSeq, Integer testPlanSeq, Integer appSeq);

    STestPlanCases findByTestCaseSeqAndEntSeq(Integer seq, Integer entSeq);

    void deleteByTestCaseSeqAndEntSeq(Integer testCaseSeq, Integer entSeq);

    List<STestPlanCases> findByTestPlanSeqAndEntSeq(Integer seq, Integer entSeq);
}
