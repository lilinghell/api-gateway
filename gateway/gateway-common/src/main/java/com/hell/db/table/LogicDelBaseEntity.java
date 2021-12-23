package com.hell.db.table;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class LogicDelBaseEntity extends BaseEntity {

    @Column(columnDefinition = "char(1) default '0' comment '数据状态:0:正常9:删除'")
    private String dataStatus;
}
