package vn.sapo.banner.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.sapo.banner.entity.BannerMapping;

import java.util.List;

@Repository
public interface BannerMappingRepository extends BaseRepository<BannerMapping, Long> {

    @Query(value = "select * from banner_mapping where section_id = ?1", nativeQuery = true)
    List<BannerMapping> getListBannerMappingBySection(Long sectionId);

    @Query(value = "select * from banner_mapping where page_id = ?1", nativeQuery = true)
    List<BannerMapping> getListBannerPopupByPage(Long pageId);

    @Modifying
    @Query(value="delete from banner_mapping where banner_id = ?1 and section_id = ?2", nativeQuery = true)
    void deleteBannerMappingViaSection(Long bannerId, Long sectionId);

    @Modifying
    @Query(value="delete from banner_mapping where banner_id = ?1 and page_id = ?2", nativeQuery = true)
    void deleteBannerMappingViaPage(Long bannerId, Long pageId);

    @Modifying
    @Query(value = "delete from banner_mapping where banner_id IN (select banners.id from banners where id = ?1)", nativeQuery = true)
    void deleteByBannerId(Long bannerId);

}
