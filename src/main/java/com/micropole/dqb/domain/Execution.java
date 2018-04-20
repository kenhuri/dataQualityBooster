package com.micropole.dqb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.micropole.dqb.domain.enumeration.Status;

/**
 * A Execution.
 */
@Entity
@Table(name = "execution")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "execution")
public class Execution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Lob
    @Column(name = "input_file")
    private byte[] inputFile;

    @Column(name = "input_file_content_type")
    private String inputFileContentType;

    @Lob
    @Column(name = "output_file")
    private byte[] outputFile;

    @Column(name = "output_file_content_type")
    private String outputFileContentType;

    @Lob
    @Column(name = "log_file")
    private byte[] logFile;

    @Column(name = "log_file_content_type")
    private String logFileContentType;

    @Column(name = "jhi_optimize")
    private Boolean optimize;

    @Column(name = "train")
    private Boolean train;

    @Column(name = "allocation")
    private Boolean allocation;

    @Lob
    @Column(name = "commentary")
    private String commentary;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    private Context context;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Execution startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Execution endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public Execution status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public byte[] getInputFile() {
        return inputFile;
    }

    public Execution inputFile(byte[] inputFile) {
        this.inputFile = inputFile;
        return this;
    }

    public void setInputFile(byte[] inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputFileContentType() {
        return inputFileContentType;
    }

    public Execution inputFileContentType(String inputFileContentType) {
        this.inputFileContentType = inputFileContentType;
        return this;
    }

    public void setInputFileContentType(String inputFileContentType) {
        this.inputFileContentType = inputFileContentType;
    }

    public byte[] getOutputFile() {
        return outputFile;
    }

    public Execution outputFile(byte[] outputFile) {
        this.outputFile = outputFile;
        return this;
    }

    public void setOutputFile(byte[] outputFile) {
        this.outputFile = outputFile;
    }

    public String getOutputFileContentType() {
        return outputFileContentType;
    }

    public Execution outputFileContentType(String outputFileContentType) {
        this.outputFileContentType = outputFileContentType;
        return this;
    }

    public void setOutputFileContentType(String outputFileContentType) {
        this.outputFileContentType = outputFileContentType;
    }

    public byte[] getLogFile() {
        return logFile;
    }

    public Execution logFile(byte[] logFile) {
        this.logFile = logFile;
        return this;
    }

    public void setLogFile(byte[] logFile) {
        this.logFile = logFile;
    }

    public String getLogFileContentType() {
        return logFileContentType;
    }

    public Execution logFileContentType(String logFileContentType) {
        this.logFileContentType = logFileContentType;
        return this;
    }

    public void setLogFileContentType(String logFileContentType) {
        this.logFileContentType = logFileContentType;
    }

    public Boolean isOptimize() {
        return optimize;
    }

    public Execution optimize(Boolean optimize) {
        this.optimize = optimize;
        return this;
    }

    public void setOptimize(Boolean optimize) {
        this.optimize = optimize;
    }

    public Boolean isTrain() {
        return train;
    }

    public Execution train(Boolean train) {
        this.train = train;
        return this;
    }

    public void setTrain(Boolean train) {
        this.train = train;
    }

    public Boolean isAllocation() {
        return allocation;
    }

    public Execution allocation(Boolean allocation) {
        this.allocation = allocation;
        return this;
    }

    public void setAllocation(Boolean allocation) {
        this.allocation = allocation;
    }

    public String getCommentary() {
        return commentary;
    }

    public Execution commentary(String commentary) {
        this.commentary = commentary;
        return this;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getUserId() {
        return userId;
    }

    public Execution userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Context getContext() {
        return context;
    }

    public Execution context(Context context) {
        this.context = context;
        return this;
    }

    public void setContext(Context context) {
        this.context = context;
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
        Execution execution = (Execution) o;
        if (execution.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), execution.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Execution{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", inputFile='" + getInputFile() + "'" +
            ", inputFileContentType='" + getInputFileContentType() + "'" +
            ", outputFile='" + getOutputFile() + "'" +
            ", outputFileContentType='" + getOutputFileContentType() + "'" +
            ", logFile='" + getLogFile() + "'" +
            ", logFileContentType='" + getLogFileContentType() + "'" +
            ", optimize='" + isOptimize() + "'" +
            ", train='" + isTrain() + "'" +
            ", allocation='" + isAllocation() + "'" +
            ", commentary='" + getCommentary() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
