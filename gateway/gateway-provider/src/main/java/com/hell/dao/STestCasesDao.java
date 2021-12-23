package com.hell.dao;

import com.hell.db.table.provider.STestCases;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface STestCasesDao extends JpaRepository<STestCases, Integer> {

    List<STestCases> findByTestGroupSeqInAndEntSeq(List<Integer> groupSeqList, Integer entSeq, Sort seq);

    STestCases findBySeqAndEntSeq(Integer seq, Integer entSeq);

    List<STestCases> findBySeqInAndEntSeq(List<Integer> caseSeq, Integer entSeq);

    List<STestCases> findByCaseTypeNotAndEntSeqAndAppSeq(String caseType2, Integer entSeq, Integer appSeq, Sort apiPath);

    List<STestCases> findByCaseTypeAndEntSeqAndAppSeq(String caseType2, Integer entSeq, Integer appSeq);

    List<STestCases> findByEntSeqAndAppSeq(Integer entSeq, Integer appSeq, Sort seq);

    STestCases findByUniKeyAndEntSeq(Integer valueOf, Integer entSeq);

    void deleteBySeqAndEntSeq(Integer seq, Integer entSeq);
}
