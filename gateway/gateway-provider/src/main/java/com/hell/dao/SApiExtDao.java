package com.hell.dao;

import com.hell.db.table.provider.SApiExt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SApiExtDao extends JpaRepository<SApiExt, Integer> {

    SApiExt findByApiSeqAndEntSeq(Integer apiSeq, Integer entSeq);

    List<SApiExt> findByEntSeq(Integer entSeq);

    List<SApiExt> findByEntSeqAndApiSeqIn(Integer entSeq, List<Integer> selectApis);
}
