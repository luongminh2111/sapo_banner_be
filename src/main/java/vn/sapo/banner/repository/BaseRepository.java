package vn.sapo.banner.repository;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import vn.sapo.banner.entity.BaseEntity;

@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity, I extends Serializable> extends JpaRepository<E, I> {
}
