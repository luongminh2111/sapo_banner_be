package vn.sapo.banner.mapper;

import org.mapstruct.Mapper;
import vn.sapo.banner.dto.SectionMappingDTO;
import vn.sapo.banner.entity.SectionMapping;

@Mapper(componentModel = "spring")
public interface SectionMappingMapper extends EntityMapper<SectionMappingDTO, SectionMapping>{
}
