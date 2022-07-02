package vn.sapo.banner.mapper;

import org.mapstruct.Mapper;
import vn.sapo.banner.dto.BannerMappingDTO;
import vn.sapo.banner.entity.BannerMapping;

@Mapper(componentModel = "spring")
public interface BannerMappingMapper extends EntityMapper<BannerMappingDTO, BannerMapping>{
}
