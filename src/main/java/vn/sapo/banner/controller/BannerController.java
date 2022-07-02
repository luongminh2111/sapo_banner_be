package vn.sapo.banner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.sapo.banner.dto.*;
import vn.sapo.banner.dto.response.BannerResponse;
import vn.sapo.banner.entity.Banner;
import vn.sapo.banner.entity.BannerMapping;
import vn.sapo.banner.entity.Section;
import vn.sapo.banner.exception.BannerException;
import vn.sapo.banner.repository.BannerRepository;
import vn.sapo.banner.service.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RestControllerAdvice
@CrossOrigin(origins = "*")
@RequestMapping("/api/banners")
public class BannerController {

    private final BannerService bannerService;
    private final BannerMappingService bannerMappingService;
    private final SectionService sectionService;
    private final SectionMappingService sectionMappingService;
    private final PageService pageService;

    @GetMapping("/{id}")
    public ResponseEntity<BannerDTO> findById(@PathVariable long id) {
        var bannerDTO = bannerService.byId(id);
        return ResponseEntity.ok(bannerDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BannerDTO>> getAllBanner() {
        List<BannerDTO> bannerDTO = bannerService.getAllBanner();
        return ResponseEntity.ok(bannerDTO);
    }
    @GetMapping("/filter/sectionId={sectionId}/{scale}")
    public ResponseEntity<List<BannerDTO>> getAllBanner(@PathVariable("sectionId") long sectionId,@PathVariable("scale") Float scale) {
        List<BannerDTO> bannerDTO = bannerService.getListBannerFilterBySectionSize(scale, sectionId);
        return ResponseEntity.ok(bannerDTO);
    }

    @GetMapping("/filter/sectionId={sectionId}")
    public ResponseEntity<List<BannerDTO>> getListBannerBySectionId(@PathVariable("sectionId") long sectionId){
        List<BannerDTO> bannerDTOList = bannerService.getListBannerBySection(sectionId);
        return ResponseEntity.ok(bannerDTOList);
    }

    //api lấy banner theo domain luôn
    @GetMapping("/by-code={code}/key={key}")
    public ResponseEntity<List<BannerResponse>> getListBannerByWebCode(@PathVariable("code") String code, @PathVariable("key") String key){
        List<PageDTO> pageEntityList = pageService.getListPageByWebCode(code, key);
        List<BannerResponse> bannerDTOList = new ArrayList<>();
        if(pageEntityList.size() > 0){
            for(int i = 0 ; i< pageEntityList.size(); i++){
                List<SectionDTO> sections = sectionService.getSectionsInPage(pageEntityList.get(i).getPageUrl());
                List<SectionMappingDTO> sectionMappings = sectionMappingService.getSectionMappingByPageUrl(pageEntityList.get(i).getPageUrl());
                // lay banner thong qua section
                for(int k=0; k< sections.size(); k++) {
                    for(int j = 0 ; j < sectionMappings.size() ; j++) {
                        if(sections.get(k).getId() == sectionMappings.get(j).getSectionId()){
                            sections.get(k).setModeHide(sectionMappings.get(j).getModeHide());
                            sections.get(k).setNumberHide(sectionMappings.get(j).getNumberHide());
                        }
                    }
                }
                for (int m = 0 ; m< sections.size(); m++) {
                    // kiểm tra trạng thái hiển thị của section(display mode): random <=> (0) hay tỉ trọng  <=> (1)
                    if (sections.get(m).getMode() == 0) {
                        BannerDTO bannerDTO = new BannerDTO();
                        try{
                            bannerDTO = bannerService.getRandomBannerInSection(sections.get(m).getId());
                        }catch (Exception e){
                            log.error("error | {} | {}",bannerDTO, e.toString());
                        }
                        if(bannerDTO != null){
                            BannerResponse bannerResponse = new BannerResponse(bannerDTO.getId(), sections.get(m).getDivId(),
                                    pageEntityList.get(i).getPageUrl(),bannerDTO.getCode(),
                                    bannerDTO.getTitle(), bannerDTO.getImgUrl(), bannerDTO.getUrl(), bannerDTO.getType(),
                                    bannerDTO.getPopUp(),sections.get(m).getWidth(), sections.get(m).getHeight(),
                                    sections.get(m).getModeHide(), sections.get(m).getNumberHide(),"0","0");
                            bannerDTOList.add(bannerResponse);
                        }

                    }
                    // nếu là tỉ trọng
                    else {
                        BannerDTO bannerDTO = new BannerDTO();
                        try {
                            bannerDTO = bannerService.getBannerByPercentageInSection(sections.get(m).getId());
                        }catch (Exception e){
                            log.error("error | {} | {}",bannerDTO, e.toString());
                        }
                        if(bannerDTO != null){
                            BannerResponse bannerResponse1 = new BannerResponse(bannerDTO.getId(),sections.get(m).getDivId(),
                                    pageEntityList.get(i).getPageUrl(),bannerDTO.getCode(),
                                    bannerDTO.getTitle(), bannerDTO.getImgUrl(), bannerDTO.getUrl(), bannerDTO.getType(),
                                    bannerDTO.getPopUp(),sections.get(m).getWidth(), sections.get(m).getHeight(),
                                    sections.get(m).getModeHide(), sections.get(m).getNumberHide(),"0","0");
                            bannerDTOList.add(bannerResponse1);
                        }
                    }
                }

                // lay banner popup truc tiep theo page neu co
                List<BannerMappingDTO> bannerMappingDTOS = bannerMappingService.getListBannerPopupByPage(pageEntityList.get(i).getId());
                if(bannerMappingDTOS.size() > 0){
                    for( int h = 0 ; h < bannerMappingDTOS.size() ; h ++) {
                        BannerDTO banner = bannerService.byId(bannerMappingDTOS.get(h).getBannerId());
                        if(banner != null){
                            BannerResponse bannerResponse = new BannerResponse(banner.getId(), "null",
                                    pageEntityList.get(i).getPageUrl(),banner.getCode(),
                                    banner.getTitle(), banner.getImgUrl(), banner.getUrl(), banner.getType(),
                                    banner.getPopUp(),banner.getWidth(), banner.getHeight(),
                                    (short)0, 0,bannerMappingDTOS.get(h).getPosition(),bannerMappingDTOS.get(h).getPositionValue());
                            bannerDTOList.add(bannerResponse);
                        }
                    }
                }

            }
        }
        return ResponseEntity.ok(bannerDTOList);
    }

    @GetMapping("/filter-has-percentage/sectionId={sectionId}")
    public ResponseEntity<List<BannerDTO>> getListBannerHasPercentageBySectionId(@PathVariable("sectionId") long sectionId){
        List<BannerDTO> firstList = bannerService.getListBannerBySection(sectionId);
        List<BannerMappingDTO> listBannerMapping = bannerMappingService.getListBannerMappingBySection(sectionId);
        for( int i = 0 ; i < firstList.size(); i++){
            for(int j = 0 ; j < listBannerMapping.size(); j ++){
                if(firstList.get(i).getId() == listBannerMapping.get(j).getBannerId()){
                    firstList.get(i).setBannerMappingId(listBannerMapping.get(j).getId());
                    firstList.get(i).setPercentage(listBannerMapping.get(j).getPercentage());
                    break;
                }
                else {
                    firstList.get(i).setBannerMappingId((long) 0);
                    firstList.get(i).setPercentage(0);
                }
            }
        }

        return ResponseEntity.ok(firstList);
    }

    // api lấy banner ngẫu nhiên theo section
    // đầu tiên sẽ phải kiểm tra sectioc là random hay percentage
    // nếu section là ramdom thì gọi hàm lấy banner theo random
    // nếu section là percentage thì gọi hàm lấy banner theo percentage
    @GetMapping("/sectionId={sectionId}/banner")
    public ResponseEntity<BannerDTO> getRandomBannerOnSection(@PathVariable("sectionId") long sectionId){
        // lấy ra section theo id
        SectionDTO section = sectionService.byId(sectionId);
        if(section != null) {
            // kiểm tra trạng thái hiển thị của section(display mode): random <=> (0) hay tỉ trọng  <=> (1)
            // nếu là random
            if (section.getMode() == 0) {
                BannerDTO banner = bannerService.getRandomBannerInSection(sectionId);
                return ResponseEntity.ok(banner);
            }
            // nếu là tỉ trọng
            else {
                BannerDTO bannerDTO = bannerService.getBannerByPercentageInSection(sectionId);
                return ResponseEntity.ok(bannerDTO);
            }
        }
        else {
            return ResponseEntity.ok(null);
        }
    }

    // api lay cac banner popUp trong cac page
    @GetMapping("/banner-popup/page={pageId}")
    public ResponseEntity<List<BannerDTO>> getListBannerPopUpByPage(@PathVariable("pageId") long pageId){
        List<BannerDTO> bannerDTOList = bannerService.getListBannerPopUpByPage(pageId);
        return ResponseEntity.ok(bannerDTOList);
    }
    // api lay cac banner popUp chua import vao page
    @GetMapping("/banner-popup/not-in-page={pageId}")
    public ResponseEntity<List<BannerDTO>> getListBannerPopUpNotInPage(@PathVariable("pageId") long pageId){
        List<BannerDTO> bannerDTOList = bannerService.getListBannerPopUpNotInPage(pageId);
        return ResponseEntity.ok(bannerDTOList);
    }
    @GetMapping("/by-code={code}")
    public ResponseEntity<BannerDTO> getBannerByCode(@PathVariable("code") String code){
        BannerDTO bannerDTO = bannerService.getByCode(code);
        return ResponseEntity.ok(bannerDTO);
    }
    @PostMapping
    public ResponseEntity<BannerDTO> createBanner(@RequestBody @Valid BannerDTO bannerDTO){
        Banner banner = new Banner(bannerDTO.getCode(), bannerDTO.getTitle(), bannerDTO.getImgUrl(), bannerDTO.getUrl(),
                bannerDTO.getType(), bannerDTO.getPopUp(), bannerDTO.getWidth(), bannerDTO.getHeight(), bannerDTO.getCreatedBy(), bannerDTO.getLastModifiedBy());
        try {
            bannerService.save(banner);
            return new ResponseEntity<BannerDTO>(bannerDTO,HttpStatus.OK);
        }catch (Exception e){
            log.error("createBanner | {} | {}", bannerDTO.toString(), e.toString());
            return new ResponseEntity<BannerDTO>(bannerDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<BannerDTO> updateBanner(@RequestBody @Valid BannerDTO bannerDTO){
        try {
            Banner banner = new Banner(bannerDTO.getId(), bannerDTO.getCode(), bannerDTO.getTitle(), bannerDTO.getImgUrl(), bannerDTO.getUrl(),
                    bannerDTO.getType(),bannerDTO.getPopUp(), bannerDTO.getWidth(), bannerDTO.getHeight(), bannerDTO.getCreatedBy(), bannerDTO.getLastModifiedBy());
            bannerService.save(banner);
            return  new ResponseEntity<BannerDTO>(bannerDTO, HttpStatus.OK);
        }catch (Exception e){
            log.error("update banner | {} | {}",bannerDTO, e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBanner (@PathVariable("id") Long id){
        try {
            bannerMappingService.deleteByBannerId(id);
            bannerService.deleteById(id);
            return new ResponseEntity<>("delete "+id + " success! ",HttpStatus.OK);
        }catch (Exception e){
            log.error("delete banner | {} | {}",id, e.toString());
            return new ResponseEntity<>("delete "+ id+ "fail",HttpStatus.BAD_REQUEST);
        }
    }
}
