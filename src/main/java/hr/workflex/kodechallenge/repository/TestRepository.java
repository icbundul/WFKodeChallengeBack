package hr.workflex.kodechallenge.repository;

import hr.workflex.kodechallenge.domain.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TestRepository extends PagingAndSortingRepository<Test, Long>, ListCrudRepository<Test, Long> {

    Page<Test> findAllByOrderByIdDesc(Pageable pageable);
}
