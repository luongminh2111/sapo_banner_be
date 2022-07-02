package vn.sapo.banner.mapper;

import org.mapstruct.Mapper;
import vn.sapo.banner.dto.SectionDTO;
import vn.sapo.banner.entity.Section;

@Mapper(componentModel = "spring")
public interface SectionMapper extends EntityMapper<SectionDTO, Section>{
}
