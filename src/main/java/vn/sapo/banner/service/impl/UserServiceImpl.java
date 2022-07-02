package vn.sapo.banner.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.sapo.banner.dto.request.LoginRequest;
import vn.sapo.banner.entity.UserEntity;
import vn.sapo.banner.mapper.UserMapper;
import vn.sapo.banner.repository.UserRepository;
import vn.sapo.banner.service.UserService;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, Long> implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(
            UserRepository userRepository, UserMapper userMapper
    ) {
        super(userRepository);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<String> getUserInfoByUsername(String username) {
        return userRepository.getUserByUserName(username);
    }

}
