package com.hell.dao;

import com.hell.db.table.provider.SApp;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SAppDao extends JpaRepository<SApp, Integer> {

    List<SApp> findByEntSeq(Integer entSeq, Sort seq);


    List<SApp> findBySeqAndEntSeq(Integer appSeq, Integer entSeq);
    SApp findBySeq(Integer Seq);

    SApp findByNameAndEntSeq(String name, Integer entSeq);
}
