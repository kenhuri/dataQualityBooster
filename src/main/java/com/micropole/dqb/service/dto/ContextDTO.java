package com.micropole.dqb.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Context entity.
 */
public class ContextDTO implements Serializable {

    private Long id;

    private String name;

    private String client;

    @Lob
    private byte[] image;
    private String imageContentType;

    private Long pythonId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Long getPythonId() {
        return pythonId;
    }

    public void setPythonId(Long pythonId) {
        this.pythonId = pythonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContextDTO contextDTO = (ContextDTO) o;
        if(contextDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contextDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContextDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", client='" + getClient() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
