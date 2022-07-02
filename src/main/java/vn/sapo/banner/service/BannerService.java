package vn.sapo.banner.service;

import vn.sapo.banner.dto.BannerDTO;
import vn.sapo.banner.entity.Banner;
import vn.sapo.banner.repository.BannerRepository;

import java.util.List;

public interface BannerService extends BaseService<Banner, Long> {
    BannerDTO byId(long id);
    List<BannerDTO> getAllBanner();
    List<BannerDTO> getListBannerFilterBySectionSize(Float scale, Long sectionId);
    List<BannerDTO> getListBannerBySection(Long sectionId);
    BannerDTO getRandomBannerInSection(Long sectionId);
    BannerDTO getBannerByPercentageInSection(Long sectionId);
    List<BannerDTO> getListBannerPopUpByPage(Long pageId);
    List<BannerDTO> getListBannerPopUpNotInPage(Long pageId);
    BannerDTO getByCode(String code);
}
