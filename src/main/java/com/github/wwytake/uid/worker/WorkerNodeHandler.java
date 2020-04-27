package com.github.wwytake.uid.worker;

import com.github.wwytake.uid.worker.entity.WorkerNodeEntity;


public interface WorkerNodeHandler {
    /**
     *
     * @param host host
     * @param port post
     * @return workNodeEntity
     */
    WorkerNodeEntity getWorkerNodeByHostPort(String host, String port);

    /**
     *
     * @param workerNodeEntity workerNodeEntity
     */
    void addWorkerNode(WorkerNodeEntity workerNodeEntity);
}
