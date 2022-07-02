package vn.sapo.banner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.sapo.banner.dto.BannerMappingDTO;
import vn.sapo.banner.entity.BannerMapping;
import vn.sapo.banner.service.BannerMappingService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RestControllerAdvice
@RequestMapping("/api/banner-mapping")
public class BannerMappingController {

    private final BannerMappingService bannerMappingService;

    @GetMapping("/{id}")
    public ResponseEntity<BannerMappingDTO> findById(@PathVariable long id) {
        var bannerMappingDTO = bannerMappingService.byId(id);
        return ResponseEntity.ok(bannerMappingDTO);
    }

    @GetMapping("/sectionId={sectionId}")
    public ResponseEntity<List<BannerMappingDTO>> findBySectionId(@PathVariable("sectionId") long sectionId){
        var bannerMapping = bannerMappingService.getListBannerMappingBySection(sectionId);
        return ResponseEntity.ok(bannerMapping);
    }
    @GetMapping("/all")
    public ResponseEntity<List<BannerMappingDTO>> getAllBanner() {
        List<BannerMappingDTO> listBannerMappingDTO = bannerMappingService.getAllBannerMapping();
        return ResponseEntity.ok(listBannerMappingDTO);
    }

    @PostMapping
    public ResponseEntity<BannerMappingDTO> createBannerMapping(@RequestBody @Valid BannerMappingDTO bannerMappingDTO){
        BannerMapping bannerMapping = new BannerMapping(bannerMappingDTO.getBannerId(), bannerMappingDTO.getSectionId(),
                bannerMappingDTO.getPageId(), bannerMappingDTO.getPosition(), bannerMappingDTO.getPositionValue(),
                bannerMappingDTO.getPercentage(), bannerMappingDTO.getCreatedBy(),
                bannerMappingDTO.getLastModifiedBy());
        try {
            bannerMappingService.save(bannerMapping);
            return new ResponseEntity<BannerMappingDTO>(bannerMappingDTO, HttpStatus.OK);
        }catch (Exception e){
            log.error("create banner mapping| {} | {}", bannerMappingDTO.toString(), e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<BannerMappingDTO> updateBannerMapping(@RequestBody @Valid BannerMappingDTO bannerMappingDTO){
        try {
            BannerMapping bannerMapping = new BannerMapping(bannerMappingDTO.getId(), bannerMappingDTO.getBannerId(),
                    bannerMappingDTO.getSectionId(), bannerMappingDTO.getPageId(),bannerMappingDTO.getPosition(), bannerMappingDTO.getPositionValue(),
                    bannerMappingDTO.getPercentage(),bannerMappingDTO.getCreatedBy(), bannerMappingDTO.getLastModifiedBy());
            bannerMappingService.save(bannerMapping);
            return  new ResponseEntity<BannerMappingDTO>(bannerMappingDTO, HttpStatus.OK);
        }catch (Exception e){
            log.error("update banner mapping| {} | {}",bannerMappingDTO, e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{bannerId}/section={sectionId}")
    public void deleteBannerMappingViaSection (@PathVariable("bannerId") Long bannerId, @PathVariable("sectionId") Long sectionId){
        bannerMappingService.deleteBannerMappingViaSection(bannerId, sectionId);
    }
    @DeleteMapping("/{bannerId}/page={pageId}")
    public void deleteBannerMappingViaPage (@PathVariable("bannerId") Long bannerId, @PathVariable("pageId") Long pageId){
        bannerMappingService.deleteBannerMappingViaPage(bannerId, pageId);
    }

}
