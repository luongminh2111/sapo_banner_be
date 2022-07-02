package vn.sapo.banner.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.sapo.banner.entity.Section;

import java.util.List;

@Repository
public interface SectionRepository extends BaseRepository<Section, Long> {
    @Query(value = "select * from sections where id IN (select section_id from section_mapping where page_id = (select id from pages where url = ?1))", nativeQuery = true)
    List<Section> getAllSectionInPage(String url);

    @Query(value="SELECT s.id, s.div_id, s.code, s.description, s.display_mode, s.width, s.height, s.created_by, s.last_modified_by,\n" +
            " s.created_at, s.last_modified_at FROM section_mapping as sm left join sections as s on sm.section_id = s.id where page_id = ?1", nativeQuery = true)
    List<Section> getSectionByPageId(long pageId);

    @Query(value="SELECT * FROM sections where (id NOT IN (select section_id from section_mapping) or id NOT IN (select section_id from section_mapping where page_id = ?1))", nativeQuery = true)
    List<Section> getSectionAvailable(long pageId);

    @Query(value = "SELECT * FROM sections where (id IN  (select section_id from section_mapping where page_id = ?1))", nativeQuery = true)
    List<Section> getAllSectionByPageId(long pageId);

    Section getByDivIdAndCode(String divId, String code);

}
