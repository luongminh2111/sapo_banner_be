package vn.sapo.banner.service;


import vn.sapo.banner.dto.SectionDTO;
import vn.sapo.banner.dto.SectionMappingDTO;
import vn.sapo.banner.entity.SectionMapping;

import java.util.List;

public interface SectionMappingService extends BaseService<SectionMapping, Long> {
    SectionMappingDTO byId(long id);
    SectionMapping save(SectionMapping section);
    List<SectionMappingDTO> getAllSection(int page, int size);
    List<SectionMappingDTO> getSectionMappingByPageUrl(String url);
    List<SectionMappingDTO> getSectionMappingByPageId(long id);
    void deleteByPageIdAndSectionId(long pageId, long sectionId);
}

