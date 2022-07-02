package vn.sapo.banner.service;

import org.springframework.transaction.annotation.Transactional;
import vn.sapo.banner.dto.BannerDTO;
import vn.sapo.banner.dto.BannerMappingDTO;
import vn.sapo.banner.entity.BannerMapping;

import java.util.List;

public interface BannerMappingService extends BaseService<BannerMapping, Long> {
    BannerMappingDTO byId(long id);
    List<BannerMappingDTO> getAllBannerMapping();
    List<BannerMappingDTO> getListBannerMappingBySection(long sectionId);
    List<BannerMappingDTO> getListBannerPopupByPage(long pageId);
    @Transactional
    void deleteBannerMappingViaSection(long bannerId, long sectionId);

    @Transactional
    void deleteBannerMappingViaPage(long bannerId, long pageId);

    @Transactional
    void deleteByBannerId(long bannerId);


}
