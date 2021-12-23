package com.hell.dao;

import com.hell.db.table.provider.STestGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface STestGroupDao extends JpaRepository<STestGroup, Integer> {

    STestGroup findBySeqAndEntSeq(Integer parentSeq, Integer entSeq);

    List<STestGroup> findByParentSeqAndEntSeq(Integer testGroupSeq, Integer entSeq);

    List<STestGroup> findByEntSeqAndAppSeq(Integer entSeq, Integer appSeq);

    void deleteBySeqAndEntSeq(Integer seq, Integer entSeq);
}
