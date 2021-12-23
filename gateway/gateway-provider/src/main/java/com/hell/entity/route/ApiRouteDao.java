package com.hell.entity.route;

import com.hell.db.table.provider.SApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface ApiRouteDao extends JpaRepository<SApi, Integer> {
    @Query(value = "select qq.*, ww.*\n" +
            "from (\n" +
            "         select a.url,\n" +
            "                a.status,\n" +
            "                a.app_env_seq as appEnvSeq,\n" +
            "                a.flow_switch      as flowSwitch,\n" +
            "                a.mock_switch      as mockSwitch,\n" +
            "                a.ent_seq          as entSeq,\n" +
            "                a.seq              as apiSeq,\n" +
            "                a.service_id       as serviceId,\n" +
            "                e.api_mock         as apiMock,\n" +
            "                e.connect_timeout  as connectTimeout,\n" +
            "                e.replenish_rate   as replenishRate,\n" +
            "                e.burst_capacity   as burstCapacity,\n" +
            "                e.response_timeout as responseTimeout\n" +
            "         from s_api a\n" +
            "                  left join s_api_ext e on a.seq = e.api_seq\n" +
            "         where a.group_seq is not null\n" +
            "           and a.url != ''\n" +
            "           and a.seq = ?1\n" +
            "           and a.status in ('0', '1')) qq\n" +
            "         left join (select address, seq as envSeq, env_key as envKey,\n" +
            "                           service_type as serviceType from s_app_env ) ww\n" +
            "                   on qq.appEnvSeq = ww.envSeq;", nativeQuery = true)
    Map<String, Object> qryApiRoute(Integer apiSeq);
}
