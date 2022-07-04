package vn.sapo.banner.service;


import org.springframework.data.domain.Page;
import vn.sapo.banner.dto.PageDTO;
import vn.sapo.banner.entity.PageEntity;

import java.util.List;

public interface PageService extends BaseService<PageEntity, Long> {

    PageDTO byId(long id);

    PageDTO byPageUrl(String url);

    List<PageDTO> findAllPage();

    Page<PageEntity> findAllPagePagination(int pageNumber);

    List<PageDTO> findPagePagination(int pageNumber, int pageSize);

    boolean saveNewPage(PageDTO pageDTO);

    boolean updatePage(PageDTO pageDTO);

    boolean deletePage(long id);

    List<PageDTO> findPageByWebsiteId(int websiteId);

    List<PageDTO> getListPageByWebCode(String code);

    List<PageDTO> getListPageByWebCode(String code, String webKey);

    Integer countByWebsiteId(int websiteId);

}
