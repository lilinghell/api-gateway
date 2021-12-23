package com.hell.dao;

import com.hell.db.table.provider.STeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface STeamDao extends JpaRepository<STeam, Integer> {

    List<STeam> findByEntSeq(Integer entSeq);

    STeam findBySeqAndEntSeq(Integer seq, Integer entSeq);
}
