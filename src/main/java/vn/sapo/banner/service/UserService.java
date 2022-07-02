package vn.sapo.banner.service;


import org.springframework.data.domain.Page;
import vn.sapo.banner.dto.request.LoginRequest;
import vn.sapo.banner.entity.UserEntity;

import java.util.List;

public interface UserService extends BaseService<UserEntity, Long> {
    List<String> getUserInfoByUsername(String username);

}

