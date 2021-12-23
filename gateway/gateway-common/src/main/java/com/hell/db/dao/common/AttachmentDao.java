package com.hell.db.dao.common;

import com.hell.db.table.common.PAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentDao extends JpaRepository<PAttachment, Integer> {

}
