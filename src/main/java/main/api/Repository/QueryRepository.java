package main.api.Repository;

import main.api.Data.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryRepository extends JpaRepository<Query, Integer> {
}
