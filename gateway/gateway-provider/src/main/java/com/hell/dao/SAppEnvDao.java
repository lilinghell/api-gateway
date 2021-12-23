package com.hell.dao;

import com.hell.db.table.provider.SAppEnv;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SAppEnvDao extends JpaRepository<SAppEnv, Integer> {
    List<SAppEnv> findByAppSeqAndEntSeq(Integer appSeq, Integer entSeq, Sort seq);

    List<SAppEnv> findBySeqAndEntSeq(Integer appEnvSeq, Integer entSeq);

    SAppEnv findByNameAndEntSeq(String name, Integer entSeq);

    SAppEnv findBySeq(Integer seq);
}
