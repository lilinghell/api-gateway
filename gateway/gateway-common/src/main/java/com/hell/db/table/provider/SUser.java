package com.hell.db.table.provider;


import com.hell.db.table.BaseEntity;
import com.hell.db.table.common.PAttachment;
import com.hell.db.table.consumer.CCifInfo;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "s_user")
@org.hibernate.annotations.Table(appliesTo = "s_user", comment = "用户表")
public class SUser extends BaseEntity {
    @Column(unique = true, columnDefinition = "varchar(64) comment '用户ID'")
    private String userId;
    @Column(columnDefinition = "varchar(128) comment '用户姓名'")
    private String userName;
    @Column(columnDefinition = "char(1) comment '用户状态0：正常、1：锁定、2：停用'")
    private String userState;
    @Column(nullable = false, columnDefinition = "varchar(64) comment '用户密码'")
    private String password;
    @Column(unique = true, columnDefinition = "varchar(128) comment '用户邮箱'")
    private String email;
    @Column(columnDefinition = "char(1) comment '邮箱是否认证 0:认证、1:非认证'")
    private String emailStatus;
    @Column(unique = true, columnDefinition = "varchar(20) comment '手机'")
    private String mobilePhone;
    @Column(columnDefinition = "varchar(5) default '+86' comment '国际区号'")
    private String globalRoaming;
    @Column(nullable = false, columnDefinition = "char(1) comment '开发者类型 0:个人 1:企业'")
    private String devType;
    @Column(nullable = false, columnDefinition = "char(1) comment '开发者类型 0:认证 1:非认证'")
    private String userType;
    @OneToOne
    @JoinColumn(name = "cifSeq", referencedColumnName = "seq")
    private CCifInfo cifInfo;
    @OneToOne
    @JoinColumn(name = "entInfoSeq", referencedColumnName = "seq")
    private SEntInfo entInfo;
    @OneToOne
    @JoinColumn(name = "attachmentSeq", referencedColumnName = "seq")
    private PAttachment headIcon;

    @Column(columnDefinition = "char(1) default '2' comment '性别: 0:男 1:女 2:保密'")
    private String gender;
    @OneToOne
    @JoinColumn(name = "roleInfoSeq", referencedColumnName = "seq")
    private SRole roleInfo;
}
