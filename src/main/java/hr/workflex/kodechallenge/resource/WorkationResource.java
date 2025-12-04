package hr.workflex.kodechallenge.resource;

import hr.workflex.kodechallenge.domain.HttpResponse;
import hr.workflex.kodechallenge.domain.Workstation;
import hr.workflex.kodechallenge.service.WorkstationService;
import hr.workflex.kodechallenge.specifications.WorkstationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping(path="/workflex")
@RequiredArgsConstructor
public class WorkationResource {

    private final WorkstationService workstationService;

    @GetMapping("/workation")
    public ResponseEntity<HttpResponse> fetchPageOfWorkstations(
            @RequestParam(required = false) Optional<String> sortColumn,
            @RequestParam(required = false) Optional<String> sortDirection,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {

        Page<Workstation> workstations = this.workstationService.findAllPaged(sortColumn.orElse(""),
                sortDirection.orElse(""),
                page.orElse(0),
                size.orElse(10));

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("page", workstations))
                        .message("Workations retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
