package com.alibaba.otter.canal.admin.model;

import io.ebean.Finder;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created on 2021/7/13.
 *
 * @author lan
 * @since 2.0.0
 */
@Entity
public class CanalAdapterConfig extends Model {

    public static final CanalAdapterConfigFinder find = new CanalAdapterConfigFinder();


    public static class CanalAdapterConfigFinder extends Finder<Long, CanalAdapterConfig> {

        public CanalAdapterConfigFinder() {
            super(CanalAdapterConfig.class);
        }
    }

    @Id
    private Long id;

    private String category;

    private String name;

    private String content;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
