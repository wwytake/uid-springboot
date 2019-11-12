/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.wwytake.uid.worker.entity;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import io.wwytake.uid.worker.WorkerNodeType;

import javax.persistence.*;

/**
 * Entity for M_WORKER_NODE
 *
 * @author yutianbao
 */
@Entity
@Table(name = "WORKER_NODE", schema = "", catalog = "")
public class WorkerNodeEntity {

    /**
     * Entity unique id (table unique)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    /**
     * Type of CONTAINER: HostName, ACTUAL : IP.
     */
    @Column(name = "HOST_NAME")
    private String hostName;

    /**
     * Type of CONTAINER: Port, ACTUAL : Timestamp + Random(0-10000)
     */
    @Column(name = "PORT")
    private String port;

    /**
     * type of {@link WorkerNodeType}
     */
    @Column(name = "TYPE")
    private int type;

    /**
     * Worker launch date, default now
     */
    @Column(name = "LAUNCH_DATE")
    private Date launchDate = new Date();

    /**
     * Created time
     */
    @Column(name = "CREATED")
    private Date created;

    /**
     * Last modified
     */
    @Column(name = "MODIFIED")
    private Date modified;

    /**
     * Getters & Setters
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDateDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
