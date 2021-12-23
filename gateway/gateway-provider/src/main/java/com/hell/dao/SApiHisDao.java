package com.hell.dao;

import com.hell.db.table.provider.SApiHis;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SApiHisDao extends JpaRepository<SApiHis, Integer> {

    List<SApiHis> findBySeqAndEntSeq(Integer apiSeq, Integer entSeq, Sort id);

    SApiHis findByIdAndEntSeq(Integer id, Integer entSeq);
}
