package com.hell.db.table.common;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "p_attachment")
@org.hibernate.annotations.Table(appliesTo = "p_attachment", comment = "附件表")
public class PAttachment extends BaseEntity {
    @Column(columnDefinition = "varchar(128) comment '原附件名'")
    String view_name;
    @Column(columnDefinition = "varchar(128) comment '本地附件名'")
    String local_name;
    @Column(columnDefinition = "varchar(512) comment '存放路径'")
    String path;
    @Column(columnDefinition = "varchar(128) comment '附件大小'")
    String size;
}
