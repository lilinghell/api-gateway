package com.hell.dao;

import com.hell.db.dao.LogicDelBaseDao;
import com.hell.db.table.provider.SRole;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface SRoleDao extends LogicDelBaseDao<SRole, Integer> {

    SRole findBySeq(Integer seq);

    List<SRole> findByRoleName(String roleName);

    List<SRole> findByRoleNameAndEntSeq(String roleName, Integer seq);

    List<SRole> findByEntSeq(Integer entSeq, Sort seq);

    List<SRole> findByRoleNameAndEntSeqAndSeqNot(String roleName, Integer entSeq, Integer seq);
}
