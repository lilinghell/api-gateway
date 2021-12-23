package com.hell.dao;

import com.hell.db.table.provider.SEntInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SEntInfoDao extends JpaRepository<SEntInfo, Integer> {
    SEntInfo findByEntCode(String entCode);
}
