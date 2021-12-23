package com.hell.dao;

import com.hell.db.table.provider.SApiServiceTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface SApiServiceTagDao extends JpaRepository<SApiServiceTag, Integer> {

    List<SApiServiceTag> findByApiSeqAndEntSeq(Integer apiSeq, Integer entSeq);

    List<SApiServiceTag> findByServiceTagSeqAndEntSeq(Integer serviceTagSeq, Integer entSeq);

    void deleteByApiSeqAndServiceTagSeqAndEntSeq(Integer apiSeq, Integer serviceTagSeq, Integer entSeq);

    @Query(value = "select distinct b.url as apiUrl, b.name as apiName from SApiServiceTag a, SApi b " +
            "where a.apiSeq =b.seq and a.serviceTagSeq=?1 and a.entSeq=?2")
    List<Map<String, Object>> QryByTagSeqAndEntSeq(Integer serviceTagSeq, Integer entSeq);

    List<SApiServiceTag> findByServiceTagSeq(Integer seq);
}
