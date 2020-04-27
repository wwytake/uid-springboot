package com.github.wwytake.uid.worker.handler;

import com.github.wwytake.uid.worker.WorkerNodeHandler;
import com.github.wwytake.uid.worker.entity.WorkerNodeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Resource;
import java.sql.*;


@Slf4j
public class DefaultWorkNodeHandler implements WorkerNodeHandler {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public WorkerNodeEntity getWorkerNodeByHostPort(String host, String port) {
        try {
            return jdbcTemplate.queryForObject("SELECT ID id,HOST_NAME hostName,PORT port,`TYPE` `type`,LAUNCH_DATE launchDate,MODIFIED modified,CREATED created FROM WORKER_NODE WHERE HOST_NAME = ? AND PORT = ?",new Object[]{host,port},WorkerNodeEntity.class);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return null;
        }
    }

    @Override
    public void addWorkerNode(WorkerNodeEntity workerNodeEntity) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps =     con.prepareStatement("INSERT INTO WORKER_NODE (HOST_NAME,PORT,`TYPE`,LAUNCH_DATE,MODIFIED,CREATED) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,workerNodeEntity.getHostName());
                ps.setString(2,workerNodeEntity.getPort());
                ps.setInt(3,workerNodeEntity.getType());
                ps.setDate(4,new Date(workerNodeEntity.getLaunchDate().getTime()));
                ps.setDate(5,new Date(workerNodeEntity.getModified().getTime()));
                ps.setDate(6,new Date(workerNodeEntity.getCreated().getTime()));

                return ps;
            }
        },keyHolder);
        workerNodeEntity.setId(keyHolder.getKey().longValue());
    }
}
