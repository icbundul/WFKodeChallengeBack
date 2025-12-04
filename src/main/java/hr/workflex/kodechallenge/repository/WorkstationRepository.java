package hr.workflex.kodechallenge.repository;

import hr.workflex.kodechallenge.domain.Workstation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkstationRepository extends PagingAndSortingRepository<Workstation, Long>, ListCrudRepository<Workstation, Long>,
        JpaSpecificationExecutor<Workstation> {

    Page<Workstation> findAllByOrderByIdDesc(Pageable pageable);
}
