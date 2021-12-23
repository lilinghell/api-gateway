package com.hell.dao;

import com.hell.db.table.provider.SMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SMessageDao extends JpaRepository<SMessage, Integer> {

    List<SMessage> findByEntSeq(Integer entSeq, Sort seq);

    SMessage findBySeqAndEntSeq(Integer seq, Integer entSeq);
}
