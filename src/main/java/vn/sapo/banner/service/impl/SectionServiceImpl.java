package vn.sapo.banner.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.sapo.banner.dto.SectionDTO;
import vn.sapo.banner.entity.Section;
import vn.sapo.banner.mapper.SectionMapper;
import vn.sapo.banner.repository.SectionRepository;
import vn.sapo.banner.service.SectionService;

import java.util.List;

@Slf4j
@Service
public class SectionServiceImpl extends BaseServiceImpl<Section, Long> implements SectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;

    public SectionServiceImpl(
            SectionRepository sectionRepository, SectionMapper sectionMapper
    ) {
        super(sectionRepository);
        this.sectionRepository = sectionRepository;
        this.sectionMapper = sectionMapper;
    }

    @Override
    public SectionDTO byId(long id) {
        var section = findById(id).orElse(null);
        return sectionMapper.toDto(section);
    }

    @Override
    public List<SectionDTO> getSectionByPageId(long id){
        var sections = sectionRepository.getSectionByPageId(id);
        return sectionMapper.toDto(sections);
    }

    @Override
    public SectionDTO getByDivIdAndCode(String divId, String code) {
        var section = sectionRepository.getByDivIdAndCode(divId, code);
        return sectionMapper.toDto(section);
    }

    @Override
    public List<SectionDTO> getSectionAvailable(long id){
        var sections = sectionRepository.getSectionAvailable(id);
        return sectionMapper.toDto(sections);
    }

    @Override
    public Section save(Section section) {
        return super.save(section);
    }

    @Override
    public List<SectionDTO> getAllSection(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Section> sections = findAll(pageable).getContent();
        return sectionMapper.toDto(sections);
    }

    @Override
    public Page<Section> getAllSectionAndPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sectionRepository.findAll(pageable);
    }

    @Override
    public List<SectionDTO> getSectionsInPage(String url) {
        List<Section> sections = sectionRepository.getAllSectionInPage(url);
        return sectionMapper.toDto(sections);
    }

    @Override
    public List<SectionDTO> getAllSectionByPageId(long pageId) {
        List<Section> sections = sectionRepository.getAllSectionByPageId(pageId);
        return sectionMapper.toDto(sections);
    }
}
