package com.micropole.dqb.service.dto;


import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Python entity.
 */
public class PythonDTO implements Serializable {

    private Long id;

    private String name;

    private String server;

    private String login;

    @Lob
    private String keySSH;

    private String defaultParameter;

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

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getKeySSH() {
        return keySSH;
    }

    public void setKeySSH(String keySSH) {
        this.keySSH = keySSH;
    }

    public String getDefaultParameter() {
        return defaultParameter;
    }

    public void setDefaultParameter(String defaultParameter) {
        this.defaultParameter = defaultParameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PythonDTO pythonDTO = (PythonDTO) o;
        if(pythonDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pythonDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PythonDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", server='" + getServer() + "'" +
            ", login='" + getLogin() + "'" +
            ", keySSH='" + getKeySSH() + "'" +
            ", defaultParameter='" + getDefaultParameter() + "'" +
            "}";
    }
}
