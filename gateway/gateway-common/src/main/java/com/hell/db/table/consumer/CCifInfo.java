package com.hell.db.table.consumer;

import com.hell.db.table.BaseEntity;
import com.hell.db.table.common.PAttachment;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "p_cif_info")
@org.hibernate.annotations.Table(appliesTo = "p_cif_info", comment = "客户表")
public class CCifInfo extends BaseEntity {

    @Column(nullable = false, columnDefinition = "varchar(32) comment '用户姓名'")
    private String name;
    @Column(nullable = false, columnDefinition = "varchar(20) comment '手机'")
    private String mobilePhone;
    @Column(nullable = false, columnDefinition = "char(1) comment '证件类型'")
    private String idType;
    @Column(nullable = false, columnDefinition = "char(1) comment '证件号'")
    private String idNo;
    @Column(nullable = false, columnDefinition = "char(1) comment '证件号 0:认证 1:未认证通过 9待认证 E 注销'")
    private String status;
    @Column(columnDefinition = "varchar(64) comment '联系地址'")
    private String addr;
    @OneToOne
    @JoinColumn(name = "file0Seq", referencedColumnName = "seq")
    private PAttachment file0;
    @OneToOne
    @JoinColumn(name = "file1Seq", referencedColumnName = "seq")
    private PAttachment file1;

}
