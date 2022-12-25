package main.api.Repository;

import main.api.Data.Group;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface GroupRepository extends PagingAndSortingRepository<Group, Integer> {
    List<Group> findAllByUserid(int user_id, Pageable pageable);
    boolean findByDbId(int id);
}
