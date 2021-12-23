package com.hell.dao;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "s_api")
@org.hibernate.annotations.Table(appliesTo = "s_api", comment = "没啥用的表")
public class ApiRouteTable implements Serializable {

    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
