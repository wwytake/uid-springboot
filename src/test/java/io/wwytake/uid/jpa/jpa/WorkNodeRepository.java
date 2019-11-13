package io.wwytake.uid.jpa.jpa;
import io.wwytake.uid.worker.entity.WorkerNodeEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author wangweiying
 */
@SuppressWarnings("unchecked")
public interface WorkNodeRepository extends CrudRepository<WorkerNodeEntity, Long> {
    /**
     *
     * @param host
     * @param port
     * @return
     */
    WorkerNodeEntity findByHostNameAndPort(String host, String port);

    /**
     *
     * @param workerNodeEntity
     * @return
     */
    @Override
    WorkerNodeEntity save(WorkerNodeEntity workerNodeEntity);
}
