package vn.sapo.banner.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.sapo.banner.dto.PageDTO;
import vn.sapo.banner.dto.WebsiteDTO;
import vn.sapo.banner.entity.WebsiteEntity;
import vn.sapo.banner.mapper.WebsiteMapper;
import vn.sapo.banner.repository.WebsiteRepository;
import vn.sapo.banner.service.WebsiteService;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class WebsiteServiceImpl extends BaseServiceImpl<WebsiteEntity, Long > implements WebsiteService {

    private final WebsiteRepository websiteRepository;
    private final WebsiteMapper websiteMapper;

    public WebsiteServiceImpl(WebsiteRepository websiteRepository, WebsiteMapper websiteMapper){
        super(websiteRepository);
        this.websiteRepository = websiteRepository;
        this.websiteMapper = websiteMapper;
    }

    @Override
    public WebsiteDTO byId(long id){
        var website = findById(id).orElse(null);
        return websiteMapper.toDto(website);
    }

    @Override
    public WebsiteDTO findByDomainAndKey(String code, String webKey) {
        var website = findByDomainAndKey(code, webKey);
        return website;
    }

    @Override
    public List<WebsiteDTO> findAllWebsite(){
        var websites = findAll();
        return websiteMapper.toDto(websites);
    }

    @Override
    public Page<WebsiteEntity> findAllWebsitePagination(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber, 5);
        var websites = findAll(pageable);
        return websites;
    }

    @Override
    public WebsiteDTO checkDomainAndWebKey(String domain){
        var website = websiteRepository.findByDomainAndWebkey(domain);
        return websiteMapper.toDto(website);
    }

    @Override
    public List<String> getWebsiteDomainByPageId(){
        var webDomains = websiteRepository.getWebsiteDomainByPage();
        return webDomains;
    }

    @Override
    public boolean deleteWebsite(long id){
        try{
            websiteRepository.deleteSectionMapByWebId(id);
            websiteRepository.deletePageByWebId(id);
            websiteRepository.deleteWeb(id);
        } catch (Exception e){
            log.error(e.toString());
            return false;
        }
        return true;
    }

    @Override
    public boolean saveNewWebsite(WebsiteDTO websiteDTO){
        var existWebsite = websiteRepository.findByCode(websiteDTO.getCode());

        if(existWebsite.isEmpty()){
            WebsiteEntity website = websiteMapper.toEntity(websiteDTO);
            save(website);
            return true;
        } else {
            System.out.println("Website code already exists.");
            return false;
        }
    }

    @Override
    public boolean updateWebsite(WebsiteDTO websiteDTO){
        var existWebsite = findById(websiteDTO.getId());

        if(existWebsite.isEmpty()){
            System.out.println("Website does not exist");
            return false;
        }

        String existWebsiteCode = existWebsite.get().getCode();
        var existWebsiteCodes = websiteRepository.findByCodeNotSameId(websiteDTO.getCode(), websiteDTO.getId());


        if(!existWebsiteCodes.isEmpty()){
            System.out.println("Duplicate code in DB");
            return false;
        }

        try{
            WebsiteEntity website = websiteMapper.toEntity(websiteDTO);
            save(website);
        } catch(Exception e){
            log.error(e.toString());
            return false;
        }
        return true;
    }


}
