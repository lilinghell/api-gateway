package com.hell.dao;

import com.hell.db.table.provider.SUser;
import com.hell.dto.request.LoginRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SUserDao extends JpaRepository<SUser, Integer> {
    @Query(value = "select u from SUser u where (u.userId = :#{#loginRequest.loginId} " +
            "or u.email = :#{#loginRequest.loginId} " +
            "or u.mobilePhone = :#{#loginRequest.loginId})" +
            "and u.password = :#{#loginRequest.loginPassword}")
    SUser qryUserInfo(@Param("loginRequest") LoginRequest loginRequest);

    SUser findByEmail(String email);

    SUser findByUserId(String userId);

    SUser findByMobilePhone(String mobilePhone);

    SUser findByEmailAndUserTypeAndEntInfoSeqAndSeqNot(String email, String userType, Integer seq, Integer seq1);

    List<SUser> findByUserTypeAndEntInfoSeqAndSeqNot(String userType, Integer seq, Integer seq1, Sort by);

    List<SUser> findByRoleInfoSeq(Integer seq);

    SUser findBySeqAndEntInfoSeq(Integer seq, Integer seq1);
}
