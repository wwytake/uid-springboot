package io.wwytake.uid.worker.myabits;

import io.wwytake.uid.worker.WorkerNodeHandler;
import io.wwytake.uid.worker.dao.WorkerNodeDAO;
import io.wwytake.uid.worker.entity.WorkerNodeEntity;

import javax.annotation.Resource;

public class WorkNodeMybatisHandler implements WorkerNodeHandler {

    @Resource
    private WorkerNodeDAO workerNodeDAO;

    @Override
    public WorkerNodeEntity getWorkerNodeByHostPort(String host, String port) {
        return workerNodeDAO.getWorkerNodeByHostPort(host,host);
    }

    @Override
    public void addWorkerNode(WorkerNodeEntity workerNodeEntity) {
        workerNodeDAO.addWorkerNode(workerNodeEntity);
    }
}
