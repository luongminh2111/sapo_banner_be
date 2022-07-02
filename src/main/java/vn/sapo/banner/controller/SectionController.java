package vn.sapo.banner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.sapo.banner.dto.SectionDTO;
import vn.sapo.banner.dto.SectionMappingDTO;
import vn.sapo.banner.entity.Section;
import vn.sapo.banner.entity.SectionMapping;
import vn.sapo.banner.service.SectionMappingService;
import vn.sapo.banner.service.SectionService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sections")
public class SectionController {

    private final SectionMappingService sectionMappingService;
    private final SectionService sectionService;

    @GetMapping("/{id}")
    public ResponseEntity<SectionDTO> findById( @PathVariable long id) {
        var sectionDTO = sectionService.byId(id);
        return ResponseEntity.ok(sectionDTO);
    }

    @GetMapping("/page-id={id}")
    public ResponseEntity<Map<String, Object>> findByPageId(@PathVariable long id){
        try {
            var sections = sectionService.getSectionByPageId(id);
            Map<String, Object> response = new HashMap<>();
            response.put("sections", sections);
            return ResponseEntity.ok(response);
        } catch(Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/available/page-id={id}")
    public ResponseEntity<List<SectionDTO>> findSectionAvailable(@PathVariable long id){
        try {
            var sections = sectionService.getSectionAvailable(id);
            return ResponseEntity.ok(sections);
        } catch(Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{page}/{size}")
    public ResponseEntity<Map<String, Object>> getAllSection(@PathVariable int page, @PathVariable int size) {
        try {
            List<SectionDTO> sections = sectionService.getAllSection(page, size);
            Page<Section> sectionPages = sectionService.getAllSectionAndPagination(page, size);
            Map<String, Object> response = new HashMap<>();
            response.put("sections", sections);
            response.put("totalSections", sectionPages.getTotalElements());
            response.put("total Page", sectionPages.getTotalPages());
            response.put("currentPage", sectionPages.getNumber());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pageId={pageId}/list-section")
    public ResponseEntity<List<SectionDTO>> getListSectionByPageId(@PathVariable("pageId") long pageId){
        var sectionDTO = sectionService.getAllSectionByPageId(pageId);
        var sectionMappings = sectionMappingService.getSectionMappingByPageId(pageId);
        for(int i = 0 ; i< sectionDTO.size(); i++){
            for(int j = 0 ; j < sectionMappings.size(); j++){
                if(sectionDTO.get(i).getId() == sectionMappings.get(j).getSectionId()){
                    sectionDTO.get(i).setSectionMappingId(sectionMappings.get(j).getId());
                    sectionDTO.get(i).setModeHide(sectionMappings.get(j).getModeHide());
                    sectionDTO.get(i).setNumberHide(sectionMappings.get(j).getNumberHide());
                }
            }
        }
        return ResponseEntity.ok(sectionDTO);
    }

    @GetMapping("/page-url={url}")
    public ResponseEntity<Map<String, Object>> getAllSectionsInPage(@PathVariable String url) {
        try {
            List<SectionDTO> sections = sectionService.getSectionsInPage(url);
            List<SectionMappingDTO> sectionMappings = sectionMappingService.getSectionMappingByPageUrl(url);
            for(int i=0; i< sections.size(); i++) {
                for(int j = 0 ; j < sectionMappings.size() ; j++) {
                    if(sections.get(i).getId() == sectionMappings.get(j).getSectionId()){
                        sections.get(i).setModeHide(sectionMappings.get(j).getModeHide());
                        sections.get(i).setNumberHide(sectionMappings.get(j).getNumberHide());
                    }
                }
            }
            Map<String, Object> response = new HashMap<>();
            response.put("sections", sections);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectionDTO> updateSection(@RequestBody @Valid SectionDTO sectionDTO, @PathVariable("id") Long id){
        try {
            Section section = new Section(id, sectionDTO.getDivId(), sectionDTO.getDesc(), sectionDTO.getCode(),sectionDTO.getMode(), sectionDTO.getWidth(), sectionDTO.getHeight(), sectionDTO.getLastModifiedBy());
            sectionService.save(section);
            return new ResponseEntity<>(sectionDTO, HttpStatus.OK);
        }catch (Exception e){
            log.error("update section | {} | {}",sectionDTO, e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping
    public ResponseEntity<SectionDTO> createSection(@RequestBody @Valid SectionDTO sectionDTO) {
        try {
            Section section = new Section(sectionDTO.getDivId(), sectionDTO.getCode(), sectionDTO.getDesc(),sectionDTO.getMode(),
                    sectionDTO.getWidth(), sectionDTO.getHeight(), sectionDTO.getCreatedBy());
            sectionService.save(section);
            return new ResponseEntity<>(sectionDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("create section | {} | {}",sectionDTO, e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteSection (@PathVariable("id") Long id){
        sectionService.deleteById(id);
    }

}
