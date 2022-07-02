package vn.sapo.banner.mapper;

import org.mapstruct.Mapper;
import vn.sapo.banner.dto.BannerDTO;
import vn.sapo.banner.entity.Banner;

@Mapper(componentModel = "spring")
public interface BannerMapper extends EntityMapper<BannerDTO, Banner>{
}
