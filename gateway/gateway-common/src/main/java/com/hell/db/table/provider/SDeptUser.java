package com.hell.db.table.provider;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "s_dept_user")
@IdClass(SDeptUser.class)
@org.hibernate.annotations.Table(appliesTo = "s_dept_user", comment = "部门成员表")
public class SDeptUser implements Serializable {
    @Id
    private Integer deptSeq;
    @Id
    private Integer memberSeq;
}
