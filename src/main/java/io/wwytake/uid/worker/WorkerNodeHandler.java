package io.wwytake.uid.worker;

import io.wwytake.uid.worker.entity.WorkerNodeEntity;


public interface WorkerNodeHandler {
    /**
     *
     * @param host
     * @param port
     * @return
     */
    WorkerNodeEntity getWorkerNodeByHostPort(String host, String port);

    /**
     *
     * @param workerNodeEntity
     */
    void addWorkerNode(WorkerNodeEntity workerNodeEntity);
}
