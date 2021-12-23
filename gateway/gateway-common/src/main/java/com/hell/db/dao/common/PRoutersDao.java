package com.hell.db.dao.common;

import com.hell.db.table.common.PRouters;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PRoutersDao extends JpaRepository<PRouters, Integer> {

    PRouters findByServiceType(String type);
}
