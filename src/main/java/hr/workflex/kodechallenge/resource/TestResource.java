package hr.workflex.kodechallenge.resource;

import hr.workflex.kodechallenge.domain.HttpResponse;
import hr.workflex.kodechallenge.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path="/test")
@RequiredArgsConstructor
public class TestResource {

    private final TestService testService;

    @GetMapping("/all")
    public ResponseEntity<HttpResponse> pingTest() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("tests", this.testService.findAllPaged(0, 10)))
                        .message("Tests retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

}
