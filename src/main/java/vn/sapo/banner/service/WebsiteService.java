package vn.sapo.banner.service;

import org.springframework.data.domain.Page;
import vn.sapo.banner.dto.WebsiteDTO;
import vn.sapo.banner.entity.WebsiteEntity;

import java.util.List;

public interface WebsiteService extends BaseService<WebsiteEntity, Long> {
    WebsiteDTO byId(long id);

    List<WebsiteDTO> findAllWebsite();

    Page<WebsiteEntity> findAllWebsitePagination(int pageNumber);

    boolean saveNewWebsite(WebsiteDTO websiteDTO);

    boolean updateWebsite(WebsiteDTO websiteDTO);

    WebsiteDTO checkDomainAndWebKey(String domain);

    WebsiteDTO findByDomainAndKey(String code, String webKey);

    List<String> getWebsiteDomainByPageId();

    boolean deleteWebsite(long webId);
}
