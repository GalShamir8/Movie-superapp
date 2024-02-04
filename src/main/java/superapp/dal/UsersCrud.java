package superapp.dal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import superapp.data.UserEntity;

import java.util.List;

public interface UsersCrud extends CrudRepository<UserEntity, String> {

    List<UserEntity> findAll(Pageable pageable);
}
