package com.hell.dao;

import com.hell.db.table.provider.STestPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface STestPlanDao extends JpaRepository<STestPlan, Integer> {
    List<STestPlan> findByAppSeqAndEntSeq(Integer appSeq, Integer entSeq);

    void deleteBySeqAndEntSeq(Integer seq, Integer entSeq);
}
