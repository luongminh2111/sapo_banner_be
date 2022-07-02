package vn.sapo.banner.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.sapo.banner.entity.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {
    @Query(value = "select u.username, u.password, r.roles from users as u inner join user_roles as ur on u.id = ur.user_id inner join roles as r on ur.role_id = r.id where u.username = ?1", nativeQuery = true)
    List<String> getUserByUserName(String username);
}
