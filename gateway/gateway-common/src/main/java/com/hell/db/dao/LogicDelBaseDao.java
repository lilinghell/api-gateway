package com.hell.db.dao;

import com.hell.db.table.LogicDelBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@NoRepositoryBean
public interface LogicDelBaseDao<T extends LogicDelBaseEntity, ID extends Serializable> extends JpaRepository<T, ID> {

    @Query("update #{#entityName} e set e.dataStatus = '9' where e.seq = ?1")
    @Transactional
    @Modifying
    void logicDeleteById(ID id);

    @Transactional
    default void logicDelete(T entity) {
        logicDeleteById((ID) entity.getSeq());
    }

    @Transactional
    default void logicDelete(Iterable<? extends T> entities) {
        entities.forEach(entity -> logicDeleteById((ID) entity.getSeq()));
    }

    @Query("update #{#entityName} e set e.dataStatus = '9' ")
    @Transactional
    @Modifying
    void logicDeleteAll();
}
