package vn.sapo.banner.service;


import org.springframework.data.domain.Page;
import vn.sapo.banner.dto.SectionDTO;
import vn.sapo.banner.entity.Section;

import java.util.List;

public interface SectionService extends BaseService<Section, Long> {
    SectionDTO byId(long id);
    Section save(Section section);
    List<SectionDTO> getAllSection(int page, int size);
    Page<Section> getAllSectionAndPagination(int page, int size);
    List<SectionDTO> getSectionsInPage(String url);
    List<SectionDTO> getSectionByPageId(long pageId);
    List<SectionDTO> getSectionAvailable(long pageId);
    List<SectionDTO> getAllSectionByPageId(long pageId);
    SectionDTO getByDivIdAndCode(String divId, String code);
}

