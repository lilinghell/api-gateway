package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import com.hell.db.table.common.PAttachment;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "s_ent_info")
@org.hibernate.annotations.Table(appliesTo = "s_ent_info", comment = "企业信息表")
public class SEntInfo extends BaseEntity {

    @Column(columnDefinition = "varchar(128) comment '企业全称'")
    private String entName;
    @Column(columnDefinition = "varchar(128) comment '企业简称'")
    private String entShortName;
    @Column(columnDefinition = "varchar(32) comment '法人名称'")
    private String corpName;
    @Column(columnDefinition = "varchar(20) comment '法人电话'")
    private String corpPhone;
    @Column(columnDefinition = "char(1) comment '法人证件类型'")
    private String corpIdType;
    @Column(columnDefinition = "varchar(20) comment '法人证件号'")
    private String corpIdNo;
    @Column(columnDefinition = "varchar(32) comment '联系人名称'")
    private String contactName;
    @Column(columnDefinition = "varchar(32) comment '联系人电话'")
    private String contactPhone;
    @Column(columnDefinition = "varchar(512) comment '企业信息详述'")
    private String entDec;
    @Column(columnDefinition = "varchar(32) comment '企业信息简述'")
    private String entShortDec;
    @Column(columnDefinition = "char(1) comment '状态0：已认证企业1：未认证企业 9：待认证 E:注销'")
    private String status;
    @Column(columnDefinition = "varchar(20) comment '组织机构代码'")
    private String entCode;
    @Column(columnDefinition = "varchar(64) comment '注册地址'")
    private String registerAddr;
    @OneToOne
    @JoinColumn(name = "entLicenseSeq", referencedColumnName = "seq")
    private PAttachment entLicenseFile;
    @OneToOne
    @JoinColumn(name = "corpFile0Seq", referencedColumnName = "seq")
    private PAttachment corpFile0;
    @OneToOne
    @JoinColumn(name = "corpFile1Seq", referencedColumnName = "seq")
    private PAttachment corpFile1;

}
