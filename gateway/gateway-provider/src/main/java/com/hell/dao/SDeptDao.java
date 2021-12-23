package com.hell.dao;

import com.hell.db.table.provider.SDept;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SDeptDao extends JpaRepository<SDept, Integer> {

    List<SDept> findByEntSeq(Integer entSeq, Sort seq);

    SDept findBySeqAndEntSeq(Integer seq, Integer entSeq);

    SDept findByDeptNameAndEntSeq(String deptName, Integer entSeq);
}
