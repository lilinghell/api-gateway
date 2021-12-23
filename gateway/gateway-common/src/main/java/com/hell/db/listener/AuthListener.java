package com.hell.db.listener;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;

public class AuthListener {
    /**
     * 在保存之前调用
     */
    @PrePersist
    public void prePersist(Object source){
        System.out.println("ok");
    }
    /**
     * 在保存之后调用
     */
    @PostPersist
    public void postPersist(Object source){
        System.out.println("ok2");
    }
}
