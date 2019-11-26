package io.wwytake.uid.worker.handler;

import io.wwytake.uid.worker.WorkerNodeHandler;
import io.wwytake.uid.worker.entity.WorkerNodeEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultWorkNodeHandler implements WorkerNodeHandler {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public WorkerNodeEntity getWorkerNodeByHostPort(String host, String port) {
        return jdbcTemplate.queryForObject("SELECT ID id,HOST_NAME hostName,PORT port,`TYPE` `type`,LAUNCH_DATE launchDate,MODIFIED modified,CREATED created FROM WORKER_NODE WHERE HOST_NAME = ? AND PORT = ?",new Object[]{host,port},WorkerNodeEntity.class);
    }

    @Override
    public void addWorkerNode(WorkerNodeEntity workerNodeEntity) {
        jdbcTemplate.update("INSERT INTO WORKER_NODE (HOST_NAME,PORT,`TYPE`,LAUNCH_DATE,MODIFIED,CREATED) VALUES (?,?,?,?,?,?)",new Object[]{workerNodeEntity.getHostName(),workerNodeEntity.getType(),workerNodeEntity.getPort(),workerNodeEntity.getLaunchDate(),workerNodeEntity.getModified(),workerNodeEntity.getCreated()});
    }
}
