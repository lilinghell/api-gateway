package com.hell.dao;

import com.hell.db.table.provider.SDeptUser;
import com.hell.db.table.provider.SUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SDeptMemberDao extends JpaRepository<SDeptUser, Integer> {
    @Query(value = "select u from SUser u, SDeptUser d where (u.seq = d.memberSeq and u.entInfo.seq = ?1" +
            " and d.deptSeq = ?2)")
    List<SUser> qryDeptMemberInfo(Integer entSeq, Integer deptSeq);

    void deleteByMemberSeqAndDeptSeq(Integer memberSeq, Integer deptSeq);

    List<SDeptUser> findByDeptSeq(Integer seq);

    void deleteByMemberSeq(Integer seq);

    List<SUser> findByMemberSeq(Integer seq);
}
