package vn.sapo.banner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.sapo.banner.entity.WebsiteEntity;

import java.util.List;

@Repository
public interface WebsiteRepository extends BaseRepository<WebsiteEntity, Long> {
    List<WebsiteEntity> findByCode(String code);

       @Query(value="select * from websites where domain = ?1 ", nativeQuery = true)
    WebsiteEntity findByDomainAndWebkey(String domain);

    @Query(value="select * from websites where code = ?1 and web_key = ?2", nativeQuery = true)
    WebsiteEntity findByDomainAndKey(String code, String webKey);


    @Query(value="select * from websites where code = ?1 and id not like ?2", nativeQuery = true)
    List<WebsiteEntity> findByCodeNotSameId(String code, long id);

    @Query(value="select websites.domain from pages left join websites on pages.website_id = websites.id order by pages.id", nativeQuery = true)
    List<String> getWebsiteDomainByPage();

//    @Modifying
//    @Transactional
//    @Query(value="delete section_mapping.* from section_mapping where page_id in (select pages.id from pages where website_id = ?1 ) \n" +
//            " delete pages.* from pages where website_id = ?1  \n" +
//            " delete websites.* from websites where id = ?1 ", nativeQuery = true)
//    void deleteWeb(long id);

    @Modifying
    @Transactional
    @Query(value="delete section_mapping.* from section_mapping where page_id in (select pages.id from pages where website_id = ?1 )"
            , nativeQuery = true)
    void deleteSectionMapByWebId(long id);

    @Modifying
    @Transactional
    @Query(value="delete pages.* from pages where website_id = ?1 ", nativeQuery = true)
    void deletePageByWebId(long id);

    @Modifying
    @Transactional
    @Query(value=" delete websites.* from websites where id = ?1 ", nativeQuery = true)
    void deleteWeb(long id);

}

