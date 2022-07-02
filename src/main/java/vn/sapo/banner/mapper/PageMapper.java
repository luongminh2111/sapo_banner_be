package vn.sapo.banner.mapper;


import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;
import vn.sapo.banner.dto.PageDTO;
import vn.sapo.banner.entity.PageEntity;

@Mapper(componentModel = "spring")
public interface PageMapper extends EntityMapper<PageDTO, PageEntity>{
}
