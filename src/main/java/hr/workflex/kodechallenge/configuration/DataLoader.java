package hr.workflex.kodechallenge.configuration;

import hr.workflex.kodechallenge.repository.WorkstationRepository;
import hr.workflex.kodechallenge.service.WorkstationCsvImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataLoader implements ApplicationRunner {

    @Value("${app.csv.import.enabled:true}")
    private boolean importEnabled;

    private final WorkstationCsvImportService csvImportService;
    private final WorkstationRepository workstationRepository;

    public DataLoader(WorkstationCsvImportService csvImportService,
                      WorkstationRepository workstationRepository) {
        this.csvImportService = csvImportService;
        this.workstationRepository = workstationRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (!this.importEnabled)
            return;

        if (workstationRepository.count() == 0) {
            log.info("Starting CSV data import...");
            csvImportService.importCsvData("data/workations.csv");
            log.info("CSV data import completed successfully!");
        } else {
            log.info("Database already contains data. Skipping CSV import.");
        }
    }

}
