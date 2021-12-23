package com.hell.dao;

import com.hell.db.table.provider.STestRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface STestRunDao extends JpaRepository<STestRun, Integer> {
    List<STestRun> findByCaseSeqInAndEntSeq(List<Integer> caseSeqList, Integer entSeq);

    STestRun findByCaseSeqAndEntSeq(Integer seq, Integer entSeq);

    void deleteByCaseSeqAndEntSeq(Integer seq, Integer entSeq);
}
