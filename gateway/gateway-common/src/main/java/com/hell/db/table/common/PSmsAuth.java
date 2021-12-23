package com.hell.db.table.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "p_sms_auth")
@IdClass(PSmsAuthPK.class)
@EntityListeners({AuditingEntityListener.class})
@org.hibernate.annotations.Table(appliesTo = "p_sms_auth", comment = "短信服务表")
public class PSmsAuth implements Serializable {

    @Id
    @Column(nullable = false, columnDefinition = "varchar(128) comment '交易码'")
    private String apiPath;

    @Id
    @Column(nullable = false, columnDefinition = "varchar(15) comment '手机'")
    private String mobilePhone;

    @Id
    @Column(nullable = false, columnDefinition = "char(1) comment '服务类别'")
    private String serviceType;

    @Column(columnDefinition = "varchar(5) default '+86' comment '国际区号'")
    private String globalRoaming;

    @Column(nullable = false, columnDefinition = "varchar(12) comment '短信验证码'")
    private String code;

    @Column(columnDefinition = "varchar(128) comment '客户mac地址'")
    private String mac;

    @Column(columnDefinition = "varchar(64) comment '客户IP'")
    private String ip;

    @Column(length = 10)
    private int num;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
