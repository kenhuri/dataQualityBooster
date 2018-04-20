package com.micropole.dqb.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Pickle entity.
 */
public class PickleDTO implements Serializable {

    private Long id;

    private String nameField;

    @Lob
    private byte[] file;
    private String fileContentType;

    private String path;

    private Long contextId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getContextId() {
        return contextId;
    }

    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PickleDTO pickleDTO = (PickleDTO) o;
        if(pickleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pickleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PickleDTO{" +
            "id=" + getId() +
            ", nameField='" + getNameField() + "'" +
            ", file='" + getFile() + "'" +
            ", path='" + getPath() + "'" +
            "}";
    }
}
