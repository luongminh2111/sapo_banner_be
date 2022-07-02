package vn.sapo.banner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import vn.sapo.banner.dto.PageDTO;
import vn.sapo.banner.entity.PageEntity;
import vn.sapo.banner.service.PageService;
import vn.sapo.banner.service.WebsiteService;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/api/pages")
public class PageController {

    private final PageService pageService;

    private final WebsiteService websiteService;

    @GetMapping("/{id}")
    public ResponseEntity<PageDTO> findById(@PathVariable long id){
        try {
            var pageDTO = pageService.byId(id);
            return ResponseEntity.ok(pageDTO);
        } catch (Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PageDTO>> findAllPages(){
        try {
            var pages = pageService.findAllPage();
            var websites = websiteService.getWebsiteDomainByPageId();
            for(int i = 0; i < pages.size(); i++) {
                pages.get(i).setWebDomain(websites.get(i));
            }
            return ResponseEntity.ok(pages);
        } catch (Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/pagination/{number}")
    public ResponseEntity<Page<PageEntity>> findAllPagesPagination(@PathVariable("number") int number){
        try {
            var pages = pageService.findAllPagePagination(number);
            return ResponseEntity.ok(pages);
        } catch(Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/pagination/{pageNumber}/{pageSize}")
    public ResponseEntity<List<PageDTO>> findPagePagination(@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
        try {
            var pages = pageService.findPagePagination(pageNumber, pageSize);
            return ResponseEntity.ok(pages);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/websiteId/{id}")
    public ResponseEntity<List<PageDTO>> findPageByWebsiteId(@PathVariable("id") int id){
        try{
            var pages = pageService.findPageByWebsiteId(id);
            return ResponseEntity.ok(pages);
        } catch(Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/web-code={code}")
    public ResponseEntity<List<PageDTO>> findPageByWebsiteCode(@PathVariable("code") String code){
        try{
            var pages = pageService.getListPageByWebCode(code);
            return ResponseEntity.ok(pages);
        } catch(Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/count/{websiteId}")
    public ResponseEntity<Integer> countByWebsiteId(@PathVariable("websiteId") int id){
        try{
            var count = pageService.countByWebsiteId(id);
            return ResponseEntity.ok(count);
        } catch(Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/add")
    public ResponseEntity<PageDTO> addPage(@RequestBody @Valid PageDTO pageDTO){
        System.out.println(pageDTO);
        try {
            boolean result = pageService.saveNewPage(pageDTO);
            if(result){
                return ResponseEntity.ok(pageDTO);
            } else{
                return new ResponseEntity<>(pageDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(pageDTO, HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/update")
    public ResponseEntity<PageDTO> updatePage(@RequestBody @Valid PageDTO pageDTO){
        try{
            boolean result = pageService.updatePage(pageDTO);
            if(result) {
                return ResponseEntity.ok(pageDTO);
            } else {
                return new ResponseEntity<>(pageDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePage(@PathVariable("id") Long id){
        try{
            pageService.deletePage(id);
            return new ResponseEntity<>("Delete successfully",HttpStatus.OK);
        } catch (Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
