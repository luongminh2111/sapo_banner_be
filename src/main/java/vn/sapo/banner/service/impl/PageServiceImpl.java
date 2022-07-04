package vn.sapo.banner.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.sapo.banner.dto.PageDTO;
import vn.sapo.banner.entity.PageEntity;
import vn.sapo.banner.mapper.PageMapper;
import vn.sapo.banner.repository.PageRepository;
import vn.sapo.banner.service.PageService;

import java.util.List;

@Slf4j
@Service
public class PageServiceImpl extends BaseServiceImpl<PageEntity, Long> implements PageService {

    private final PageRepository pageRepository;
    private final PageMapper pageMapper;

    public PageServiceImpl(
            PageRepository pageRepository, PageMapper pageMapper
    ){
        super(pageRepository);
        this.pageRepository = pageRepository;
        this.pageMapper = pageMapper;
    }

    @Override
    public PageDTO byId(long id) {
        var page = findById(id).orElse(null);
        return pageMapper.toDto(page);
    }

    @Override
    public PageDTO byPageUrl(String url){
        var page = pageRepository.findByPageUrl(url);
        return pageMapper.toDto(page);
    }


    @Override
    public List<PageDTO> findAllPage(){
        var pages = findAll();
        return pageMapper.toDto(pages);
    }

    @Override
    public Page<PageEntity> findAllPagePagination(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber, 5);
        return findAll(pageable);
    }


    @Override
    public List<PageDTO> findPagePagination(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        var pages = findAll(pageable).getContent();
        return pageMapper.toDto(pages);
    }

    @Override
    public List<PageDTO> findPageByWebsiteId(int websiteId){
        var pages = pageRepository.getByWebsiteId(websiteId);
        return pageMapper.toDto(pages);
    }
    @Override
    public List<PageDTO> getListPageByWebCode(String code) {
        List<PageEntity> pageEntityList = pageRepository.getListPageByWebCode(code);
        return pageMapper.toDto(pageEntityList);
    }
    @Override
    public List<PageDTO> getListPageByWebCode(String code, String webKey) {
        List<PageEntity> listPage = pageRepository.getByWebsiteCode(code, webKey);
        return pageMapper.toDto(listPage);
    }

    @Override
    public Integer countByWebsiteId(int websiteId){
        var count = pageRepository.countByWebsiteId(websiteId);
        return count;
    }

    @Override
    public boolean deletePage(long id){
        try {
            pageRepository.deleteSectionMappingByPage(id);
            pageRepository.deletePage(id);
        } catch (Exception e){
            log.error(e.toString());
            return false;
        }
        return true;
    }

    @Override
    public boolean saveNewPage(PageDTO pageDTO){
        var existPages = pageRepository.findByPageName(pageDTO.getPageName());

        if(existPages.isEmpty()){
            PageEntity page = pageMapper.toEntity(pageDTO);
            save(page);
            return true;
        } else {
            System.out.println("Page name already exists.");
            return false;
        }

    }

    @Override
    public boolean updatePage(PageDTO pageDTO) {
        var existPage = findById(pageDTO.getId());

        if(existPage.isEmpty()) {
            System.out.println("Page does not exist");
            return false;
        }

        var existPageUrls = pageRepository.findByUrlNotSameId(pageDTO.getPageUrl(), pageDTO.getId());
        if(!existPageUrls.isEmpty()){
            System.out.println("Duplicate page url in DB");
            return false;
        }

        try{
            PageEntity page = pageMapper.toEntity(pageDTO);
            save(page);
        } catch(Exception e){
            log.error(e.toString());
            return false;
        }
        return true;

    }

}
