package vn.sapo.banner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.sapo.banner.entity.PageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface PageRepository extends BaseRepository<PageEntity, Long> {

//    @Query(value = "select * from pages where website_id = ?1", nativeQuery = true)
//    Page<PageEntity> getBannerByUserAdd(int websiteId, Pageable pageable);
    List<PageEntity> findByPageName(String name);

    @Query(value = "select * from pages where name = ?1 and id not like ?2", nativeQuery = true)
    List<PageEntity> getByPageName(String name, long id);

    PageEntity findByPageUrl(String pageUrl);

    List<PageEntity> getByWebsiteId(int id);

    @Query(value="select * from pages where url = ?1 and id not like ?2", nativeQuery = true)
    List<PageEntity> findByUrlNotSameId(String url, long id);

    @Query(value = "select count(id) from pages where website_id = ?1", nativeQuery = true)
    int countByWebsiteId(long websiteId);

    @Query(value = "SELECT pages.* FROM pages left join websites on pages.website_id = websites.id where websites.code = ?1 and websites.web_key =?2", nativeQuery = true)
    List<PageEntity> getByWebsiteCode(String code, String webKey);

    @Query(value = "SELECT pages.* FROM pages left join websites on pages.website_id = websites.id where websites.code = ?1", nativeQuery = true)
    List<PageEntity> getListPageByWebCode(String code);

    @Modifying
    @Transactional
    @Query(value = "delete pages.* from pages where id = ?1 ", nativeQuery = true)
    void deletePage(long id);

    @Modifying
    @Transactional
    @Query(value = "delete section_mapping.* from section_mapping where page_id = ?1", nativeQuery = true)
    void deleteSectionMappingByPage(long id);
}
