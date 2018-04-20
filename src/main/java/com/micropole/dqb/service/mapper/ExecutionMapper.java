package com.micropole.dqb.service.mapper;

import com.micropole.dqb.domain.*;
import com.micropole.dqb.service.dto.ExecutionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Execution and its DTO ExecutionDTO.
 */
@Mapper(componentModel = "spring", uses = {ContextMapper.class})
public interface ExecutionMapper extends EntityMapper<ExecutionDTO, Execution> {

    @Mapping(source = "context.id", target = "contextId")
    ExecutionDTO toDto(Execution execution);

    @Mapping(source = "contextId", target = "context")
    Execution toEntity(ExecutionDTO executionDTO);

    default Execution fromId(Long id) {
        if (id == null) {
            return null;
        }
        Execution execution = new Execution();
        execution.setId(id);
        return execution;
    }
}
