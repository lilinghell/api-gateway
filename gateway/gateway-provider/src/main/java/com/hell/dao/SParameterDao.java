package com.hell.dao;

import com.hell.db.table.provider.SParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SParameterDao extends JpaRepository<SParameter, Integer> {
    void deleteBySeqAndEntSeq(Integer seq, Integer entSeq);

    SParameter findBySeqAndEntSeq(Integer seq, Integer entSeq);

    SParameter findByNameAndAppSeqAndEntSeq(String name, Integer appSeq, Integer entSeq);

    SParameter findByEnNameAndAppSeqAndEntSeq(String enName, Integer appSeq, Integer entSeq);

    List<SParameter> findByAppSeqAndEntSeq(Integer appSeq, Integer entSeq, Sort seq);

    SParameter findByEnNameAndAppSeqAndTypeAndEntSeq(String qryKey, Integer appSeq, String s, Integer entSeq);

    List<SParameter> findByAppSeqAndTypeAndEntSeq(Integer appSeq, String s, Integer entSeq);
}
