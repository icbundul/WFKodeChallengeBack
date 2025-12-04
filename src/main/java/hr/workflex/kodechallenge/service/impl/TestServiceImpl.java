package hr.workflex.kodechallenge.service.impl;

import hr.workflex.kodechallenge.domain.Test;
import hr.workflex.kodechallenge.repository.TestRepository;
import hr.workflex.kodechallenge.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.PageRequest.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    @Override
    public Page<Test> findAllPaged(int page, int size) {
        return this.testRepository.findAllByOrderByIdDesc(of(page, size));
    }
}
