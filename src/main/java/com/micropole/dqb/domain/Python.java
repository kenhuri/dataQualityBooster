package com.micropole.dqb.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "python")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "python")
public class Python implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "server")
    private String server;

    @Column(name = "login")
    private String login;

    @Lob
    @Column(name = "key_ssh")
    private String keySSH;

    @Column(name = "default_parameter")
    private String defaultParameter;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Python name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public Python server(String server) {
        this.server = server;
        return this;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLogin() {
        return login;
    }

    public Python login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getKeySSH() {
        return keySSH;
    }

    public Python keySSH(String keySSH) {
        this.keySSH = keySSH;
        return this;
    }

    public void setKeySSH(String keySSH) {
        this.keySSH = keySSH;
    }

    public String getDefaultParameter() {
        return defaultParameter;
    }

    public Python defaultParameter(String defaultParameter) {
        this.defaultParameter = defaultParameter;
        return this;
    }

    public void setDefaultParameter(String defaultParameter) {
        this.defaultParameter = defaultParameter;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Python python = (Python) o;
        if (python.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), python.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Python{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", server='" + getServer() + "'" +
            ", login='" + getLogin() + "'" +
            ", keySSH='" + getKeySSH() + "'" +
            ", defaultParameter='" + getDefaultParameter() + "'" +
            "}";
    }
}
