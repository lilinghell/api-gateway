package com.hell.dao;

import com.hell.db.table.provider.SServiceTag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SServiceTagDao extends JpaRepository<SServiceTag, Integer> {
    List<SServiceTag> findByAppSeqAndEntSeq(Integer appSeq, Integer entSeq, Sort seq);

    SServiceTag findByNameAndEntSeq(String name, Integer entSeq);

    SServiceTag findBySeq(Integer seq);
}
