package com.micropole.dqb.service.mapper;

import com.micropole.dqb.domain.*;
import com.micropole.dqb.service.dto.ContextDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Context and its DTO ContextDTO.
 */
@Mapper(componentModel = "spring", uses = {PythonMapper.class})
public interface ContextMapper extends EntityMapper<ContextDTO, Context> {

    @Mapping(source = "python.id", target = "pythonId")
    ContextDTO toDto(Context context);

    @Mapping(source = "pythonId", target = "python")
    Context toEntity(ContextDTO contextDTO);

    default Context fromId(Long id) {
        if (id == null) {
            return null;
        }
        Context context = new Context();
        context.setId(id);
        return context;
    }
}
