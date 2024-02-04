package superapp.dal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import superapp.data.SuperAppObjectEntity;


import java.util.Date;
import java.util.List;

public interface SuperAppObjectCrud extends PagingAndSortingRepository<SuperAppObjectEntity, String> {
    public List<SuperAppObjectEntity> findAllByType(String type, Pageable pageable);

    public List<SuperAppObjectEntity> findAllByAlias(String alias, Pageable pageable);

    public List<SuperAppObjectEntity> findAllByAliasLike(String alias, Pageable pageable);
    public List<SuperAppObjectEntity> findAllByCreatedAtBetween(Date start, Date end, Pageable pageable);
}
