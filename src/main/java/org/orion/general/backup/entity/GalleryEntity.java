package org.orion.general.backup.entity;

import org.orion.common.basic.BaseEntity;

public class GalleryEntity extends BaseEntity {
    public GalleryEntity() {
        super("GF_BACKUP_GALLERY_TX", "");
    }

    private long PicId;
    private String name;
    private String fileName;
    private String category;
    private String description;
    private long size;
    private byte[] content;
    private String hash;

    public long getPicId() {
        return PicId;
    }

    public void setPicId(long picId) {
        PicId = picId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
