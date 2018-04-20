package com.micropole.dqb.service.mapper;

import com.micropole.dqb.domain.*;
import com.micropole.dqb.service.dto.PickleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Pickle and its DTO PickleDTO.
 */
@Mapper(componentModel = "spring", uses = {ContextMapper.class})
public interface PickleMapper extends EntityMapper<PickleDTO, Pickle> {

    @Mapping(source = "context.id", target = "contextId")
    PickleDTO toDto(Pickle pickle);

    @Mapping(source = "contextId", target = "context")
    Pickle toEntity(PickleDTO pickleDTO);

    default Pickle fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pickle pickle = new Pickle();
        pickle.setId(id);
        return pickle;
    }
}
