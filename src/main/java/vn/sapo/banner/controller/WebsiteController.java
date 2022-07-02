package vn.sapo.banner.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.sapo.banner.dto.WebsiteDTO;
import vn.sapo.banner.entity.WebsiteEntity;
import vn.sapo.banner.service.WebsiteService;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/api/websites")
public class WebsiteController {
    private final WebsiteService websiteService;


    @GetMapping("/{id}")
    public ResponseEntity<WebsiteDTO> findById(@PathVariable long id){
        try {
            var websiteDTO = websiteService.byId(id);
            return ResponseEntity.ok(websiteDTO);
        } catch (Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<WebsiteDTO>> findAllWebsites(){
        try {
            var websites = websiteService.findAllWebsite();
            return ResponseEntity.ok(websites);
        } catch (Exception e){
            e.printStackTrace();
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/pagination/{number}")
    public ResponseEntity<Page<WebsiteEntity>> findAllWebsitePagination(@PathVariable("number") int number){
        try{
            var websites = websiteService.findAllWebsitePagination(number);
            return ResponseEntity.ok(websites);
        } catch(Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/domain={domain}")
    public ResponseEntity<WebsiteDTO> findByDomainAndWebKey(@PathVariable("domain") String domain){
        try {
            var website = websiteService.checkDomainAndWebKey(domain);
            return ResponseEntity.ok(website);
        } catch(Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list/web-domain")
    public ResponseEntity<List<String>> findWebDomainByPageId(){
        try{
            var webDomain = websiteService.getWebsiteDomainByPageId();
            return ResponseEntity.ok(webDomain);
        } catch (Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<WebsiteDTO> addWebsite(@RequestBody @Valid WebsiteDTO websiteDTO){
        try{
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            KeySpec spec = new PBEKeySpec(websiteDTO.getWebKey().toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            Base64.Encoder enc = Base64.getEncoder();
            websiteDTO.setWebKey(enc.encodeToString(hash));
            boolean result = websiteService.saveNewWebsite(websiteDTO);
            if(result){
                return ResponseEntity.ok(websiteDTO);
            } else {
                return new ResponseEntity<>(websiteDTO, HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e){
                log.error(e.toString());
                return new ResponseEntity<>(websiteDTO, HttpStatus.BAD_REQUEST);

        }

    }

    @PutMapping("/update")
    public ResponseEntity<WebsiteDTO> updatePage(@RequestBody @Valid WebsiteDTO websiteDTO){
        try{
            boolean result = websiteService.updateWebsite(websiteDTO);
            if(result){
                return ResponseEntity.ok(websiteDTO);
            } else {
                return new ResponseEntity<>(websiteDTO, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
                log.error(e.toString());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteWebsite(@PathVariable("id") Long id){
//        try{
//            websiteService.deleteById(id);
//            return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
//        } catch (Exception e){
//            log.error(e.toString());
//            return new ResponseEntity<>("Error.", HttpStatus.BAD_REQUEST);
//        }
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteWebsite(@PathVariable("id") Long id){
        try{
            websiteService.deleteWebsite(id);
            return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
        } catch (Exception e){
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
