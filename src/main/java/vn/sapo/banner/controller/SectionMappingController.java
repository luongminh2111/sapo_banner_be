package vn.sapo.banner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.sapo.banner.dto.SectionDTO;
import vn.sapo.banner.dto.SectionMappingDTO;
import vn.sapo.banner.dto.request.SectionMappingRequest;
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
@RequestMapping("/api/section-mapping")
public class SectionMappingController {

    private final SectionMappingService sectionMappingService;
    private final SectionService sectionService;

    @GetMapping("/{id}")
    public ResponseEntity<SectionMappingDTO> findById(
            @PathVariable long id
    ) {
        var sectionMappingDTO = sectionMappingService.byId(id);
        return ResponseEntity.ok(sectionMappingDTO);
    }

    @GetMapping("/page={page}/size={size}")
    public ResponseEntity<Map<String, Object>> getAllSectionMapping(@PathVariable int page, @PathVariable int size) {
        try {
            List<SectionMappingDTO> sections = sectionMappingService.getAllSection(page, size);
            Map<String, Object> response = new HashMap<>();
            response.put("sections", sections);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<SectionMappingDTO> updateSectionMapping(@RequestBody @Valid SectionMappingDTO sectionMappingDTO){
        try {
            SectionMapping sectionMapping = new SectionMapping(sectionMappingDTO.getId(), sectionMappingDTO.getPageId(),
                    sectionMappingDTO.getSectionId(),sectionMappingDTO.getModeHide(), sectionMappingDTO.getNumberHide(),
                    sectionMappingDTO.getCreatedBy(), sectionMappingDTO.getLastModifiedBy());
            sectionMappingService.save(sectionMapping);
            return new ResponseEntity<>(sectionMappingDTO, HttpStatus.OK);
        }catch (Exception e){
            log.error("update section mapping | {} | {}",sectionMappingDTO, e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<SectionMappingRequest> createSectionMapping(@RequestBody @Valid SectionMappingRequest sectionMappingRequest) {
        try {
            SectionDTO sectionDTO = sectionService.getByDivIdAndCode(sectionMappingRequest.getDivId(), sectionMappingRequest.getSectionCode());
            SectionMapping sectionMapping = new SectionMapping(sectionMappingRequest.getPageId(), sectionDTO.getId(),
                    sectionMappingRequest.getModeHide(), sectionMappingRequest.getNumberHide(),
                    sectionMappingRequest.getCreatedBy(), sectionMappingRequest.getLastModifiedBy());
            sectionMappingService.save(sectionMapping);
            return new ResponseEntity<>(sectionMappingRequest, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("create section mapping| {} | {}",sectionMappingRequest, e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/page-url={url}")
    public ResponseEntity<Map<String, Object>> getSectionMappingByPageUrl(@PathVariable String url) {
        try {
            List<SectionMappingDTO> sections = sectionMappingService.getSectionMappingByPageUrl(url);
            Map<String, Object> response = new HashMap<>();
            response.put("sections", sections);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteSectionMapping (@PathVariable("id") Long id){
        sectionMappingService.deleteById(id);
    }

    @Transactional
    @DeleteMapping("/{pageId}/{sectionId}")
    public void deleteByPageIdAndSectionId(@Valid @PathVariable("pageId") long pageId,@Valid @PathVariable("sectionId") long sectionId){
        try {
            sectionMappingService.deleteByPageIdAndSectionId(pageId, sectionId);
        }
        catch (Exception e){
            log.error("delete section mapping false : | {} | {} | {}",pageId, sectionId, HttpStatus.NOT_FOUND );
        }
    }
}
