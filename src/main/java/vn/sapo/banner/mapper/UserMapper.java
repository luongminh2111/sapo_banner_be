package vn.sapo.banner.mapper;

import org.mapstruct.Mapper;
import vn.sapo.banner.dto.request.LoginRequest;
import vn.sapo.banner.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<LoginRequest, UserEntity>{
}
