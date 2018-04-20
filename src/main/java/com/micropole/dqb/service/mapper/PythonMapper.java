package com.micropole.dqb.service.mapper;

import com.micropole.dqb.domain.*;
import com.micropole.dqb.service.dto.PythonDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Python and its DTO PythonDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PythonMapper extends EntityMapper<PythonDTO, Python> {



    default Python fromId(Long id) {
        if (id == null) {
            return null;
        }
        Python python = new Python();
        python.setId(id);
        return python;
    }
}
