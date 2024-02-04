package superapp.dal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import superapp.data.MiniAppCommandEntity;

import java.util.List;

public interface MiniAppCommandCrud extends PagingAndSortingRepository<MiniAppCommandEntity, String> {

    List<MiniAppCommandEntity> findAllByCommand(Pageable pageable, String command);
    List<MiniAppCommandEntity> findAllByCommand(String command);

}
