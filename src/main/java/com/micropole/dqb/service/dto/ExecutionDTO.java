package com.micropole.dqb.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import com.micropole.dqb.domain.enumeration.Status;

/**
 * A DTO for the Execution entity.
 */
public class ExecutionDTO implements Serializable {

    private Long id;

    private Instant startDate;

    private Instant endDate;

    private Status status;

    @Lob
    private byte[] inputFile;
    private String inputFileContentType;

    @Lob
    private byte[] outputFile;
    private String outputFileContentType;

    @Lob
    private byte[] logFile;
    private String logFileContentType;

    private Boolean optimize;

    private Boolean train;

    private Boolean allocation;

    @Lob
    private String commentary;

    private String userId;

    private Long contextId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public byte[] getInputFile() {
        return inputFile;
    }

    public void setInputFile(byte[] inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputFileContentType() {
        return inputFileContentType;
    }

    public void setInputFileContentType(String inputFileContentType) {
        this.inputFileContentType = inputFileContentType;
    }

    public byte[] getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(byte[] outputFile) {
        this.outputFile = outputFile;
    }

    public String getOutputFileContentType() {
        return outputFileContentType;
    }

    public void setOutputFileContentType(String outputFileContentType) {
        this.outputFileContentType = outputFileContentType;
    }

    public byte[] getLogFile() {
        return logFile;
    }

    public void setLogFile(byte[] logFile) {
        this.logFile = logFile;
    }

    public String getLogFileContentType() {
        return logFileContentType;
    }

    public void setLogFileContentType(String logFileContentType) {
        this.logFileContentType = logFileContentType;
    }

    public Boolean isOptimize() {
        return optimize;
    }

    public void setOptimize(Boolean optimize) {
        this.optimize = optimize;
    }

    public Boolean isTrain() {
        return train;
    }

    public void setTrain(Boolean train) {
        this.train = train;
    }

    public Boolean isAllocation() {
        return allocation;
    }

    public void setAllocation(Boolean allocation) {
        this.allocation = allocation;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

        ExecutionDTO executionDTO = (ExecutionDTO) o;
        if(executionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), executionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExecutionDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", inputFile='" + getInputFile() + "'" +
            ", outputFile='" + getOutputFile() + "'" +
            ", logFile='" + getLogFile() + "'" +
            ", optimize='" + isOptimize() + "'" +
            ", train='" + isTrain() + "'" +
            ", allocation='" + isAllocation() + "'" +
            ", commentary='" + getCommentary() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
