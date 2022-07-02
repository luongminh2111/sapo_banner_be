package vn.sapo.banner.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.sapo.banner.entity.Banner;

import java.util.List;

@Repository
public interface BannerRepository extends BaseRepository<Banner, Long> {

    // lấy ra các banner chưa có trong section phù hợp để thêm vào section dựa theo sectionId và kích cỡ của section
    @Query(value = "select * from banners where (width/height)- 0.2 < ?1 and (width/height) + 0.2 > ?1 and (banners.id NOT IN( select banner_mapping.banner_id from banner_mapping where banner_mapping.section_id = ?2)) and banners.pop_up != 1", nativeQuery = true)
    List<Banner> getListBannerFilterBySectionSize(Float scale, Long sectionId);

    // lấy ra các banner đã có trong section theo section ID
    @Query(value = "select * from banners where (banners.id IN( select banner_mapping.banner_id from banner_mapping where banner_mapping.section_id = ?1)) and banners.pop_up != 1", nativeQuery = true)
    List<Banner> getListBannerBySection(Long sectionId);

    // lấy random một banner trong section
    @Query(value = "select banners.* from banner_mapping left join banners on banner_mapping.banner_id = banners.id where section_id = ?1 order by rand() limit 1", nativeQuery = true)
    Banner getRandomBannerInSection(Long sectionId);

    // lay ra cac banner popup trong các page
    @Query(value = "SELECT DISTINCT banners.* FROM banners left join banner_mapping on banners.id = banner_mapping.banner_id where banner_id IN ( select id from banners where pop_up = 1) and banner_mapping.page_id =?1", nativeQuery = true)
    List<Banner> getListBannerPopUpByPage(Long pageId);

    // lay ra cac banner popup trong chua gan vao page
    @Query(value = "SELECT  banners.* FROM banners where banners. pop_up = 1 and banners.id NOT IN (select banner_mapping.banner_id from banner_mapping where banner_mapping.page_id = ?1)", nativeQuery = true)
    List<Banner> getListBannerPopUpNotInPage(Long pageId);

    Banner getByCode(String code);
}
