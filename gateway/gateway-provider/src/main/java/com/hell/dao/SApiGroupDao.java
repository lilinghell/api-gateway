package com.hell.dao;

import com.hell.db.table.provider.SApiGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SApiGroupDao extends JpaRepository<SApiGroup, Integer> {
    void deleteBySeqAndEntSeq(Integer seq, Integer entSeq);

    SApiGroup findBySeqAndEntSeq(Integer seq, Integer entSeq);

    List<SApiGroup> findByParentSeqAndEntSeq(Integer seq, Integer entSeq);

    @Query(value = "select *\n" +
            "from s_api_group\n" +
            "where seq = ?1\n" +
            "  and ent_seq = ?2\n" +
            "union\n" +
            "select *\n" +
            "from s_api_group\n" +
            "where parent_seq = ?1\n" +
            "  and ent_seq = ?2\n" +
            "union\n" +
            "select *\n" +
            "from s_api_group\n" +
            "where seq = (select a.parent_seq from s_api_group a where a.seq = ?1 and a.ent_seq = ?2)", nativeQuery = true)
    List<SApiGroup> qrySubAndParent(Integer groupSeq, Integer entSeq, Integer appSeq);

    SApiGroup findByNameAndEntSeqAndAppSeq(String name, Integer entSeq, Integer appSeq);

    List<SApiGroup> findByEntSeqAndAppSeq(Integer entSeq, Integer appSeq);
}
