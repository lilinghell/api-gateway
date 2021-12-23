package com.hell.dao;

import com.hell.db.table.provider.SApi;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SApiDao extends JpaRepository<SApi, Integer> {

    SApi findBySeqAndEntSeq(Integer seq, Integer entSeq);

    void deleteBySeqAndEntSeq(Integer seq, Integer entSeq);

    List<SApi> findByEntSeqAndSeqIn(Integer entSeq, List<Integer> selectApis);

    SApi findByUrlAndEntSeqAndAppEnvSeq(String url, Integer entSeq, Integer appEnvSeq);

    List<SApi> findByEntSeqAndAppEnvSeq(Integer entSeq, Integer appEnvSeq, Sort seq);

    SApi findByUrlAndAppEnvSeq(String url, Integer appEnvSeq);

    List<SApi> findByGroupSeqInAndEntSeqAndAppEnvSeq(List<Integer> groupSeqList, Integer entSeq, Integer appEnvSeq, Sort seq);

    List<SApi> findByGroupSeqAndEntSeqAndAppEnvSeq(Integer seq, Integer entSeq, Integer appEnvSeq);

    List<SApi> findBySeqInAndEntSeqAndAppEnvSeq(List<Integer> apiSeqList, Integer entSeq, Integer envSeq);

    SApi findByNameAndGroupSeqAndEntSeqAndAppEnvSeq(String name, Integer groupSeq, Integer entSeq, Integer appEnvSeq);
}
