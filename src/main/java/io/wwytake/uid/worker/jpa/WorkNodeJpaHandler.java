package io.wwytake.uid.worker.jpa;

import io.wwytake.uid.worker.WorkerNodeHandler;
import io.wwytake.uid.worker.entity.WorkerNodeEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkNodeJpaHandler implements WorkerNodeHandler {

    @Autowired
    private WorkNodeRepository workNodeRepository;


    @Override
    public WorkerNodeEntity getWorkerNodeByHostPort(String host, String port) {
        return workNodeRepository.findByHostNameAndPort(host,port);
    }

    @Override
    public void addWorkerNode(WorkerNodeEntity workerNodeEntity) {
        workNodeRepository.save(workerNodeEntity);
    }
}
