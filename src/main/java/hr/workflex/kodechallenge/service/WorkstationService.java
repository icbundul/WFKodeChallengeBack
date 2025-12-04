package hr.workflex.kodechallenge.service;

import hr.workflex.kodechallenge.domain.Workstation;
import hr.workflex.kodechallenge.repository.WorkstationRepository;
import hr.workflex.kodechallenge.specifications.WorkstationSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.PageRequest.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkstationService {

    private final WorkstationRepository workstationRepository;

    public Page<Workstation> findAllPaged(String sortColumn, String sortDirection, int page, int size) {
        return this.workstationRepository.findAll(WorkstationSpecification.filterBy(sortColumn, sortDirection), of(page, size));
    }
}
