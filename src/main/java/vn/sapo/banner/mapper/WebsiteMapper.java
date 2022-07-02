package vn.sapo.banner.mapper;

import org.mapstruct.Mapper;
import vn.sapo.banner.dto.BannerDTO;
import vn.sapo.banner.dto.WebsiteDTO;
import vn.sapo.banner.entity.WebsiteEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WebsiteMapper extends EntityMapper<WebsiteDTO, WebsiteEntity> {

}
