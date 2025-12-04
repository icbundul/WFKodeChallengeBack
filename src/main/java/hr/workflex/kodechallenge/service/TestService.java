package hr.workflex.kodechallenge.service;

import hr.workflex.kodechallenge.domain.Test;
import org.springframework.data.domain.Page;

public interface TestService {

    Page<Test> findAllPaged(int page, int size);
}
