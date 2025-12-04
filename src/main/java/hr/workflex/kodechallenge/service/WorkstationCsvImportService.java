package hr.workflex.kodechallenge.service;

import hr.workflex.kodechallenge.domain.Workstation;
import hr.workflex.kodechallenge.repository.WorkstationRepository;
import hr.workflex.kodechallenge.util.DateUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WorkstationCsvImportService {

    private final WorkstationRepository workstationRepository;

    public WorkstationCsvImportService(WorkstationRepository workstationRepository) {
        this.workstationRepository = workstationRepository;
    }

    @Transactional
    public void importCsvData(String csvFilePath) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(csvFilePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + csvFilePath);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());

        List<Workstation> workstations = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (CSVRecord record : csvParser) {
            Workstation workstation = Workstation.builder()
                    .workstationId(record.get("workationId"))
                    .employee(record.get("employee"))
                    .origin(record.get("origin"))
                    .destination(record.get("destination"))
                    .start(parseDate(record.get("start"), dateFormat))
                    .end(parseDate(record.get("end"), dateFormat))
                    .risk(record.get("risk"))
                    .build();

            workstation.setWorkingDays(DateUtil.getWorkingDays(workstation.getStart(), workstation.getEnd()));
            workstations.add(workstation);
        }

        workstationRepository.saveAll(workstations);
        csvParser.close();
        reader.close();
    }

    private Date parseDate(String dateStr, SimpleDateFormat dateFormat) {
        try {
            return dateStr != null && !dateStr.isEmpty() ? dateFormat.parse(dateStr) : null;
        } catch (Exception e) {
            return null;
        }
    }

}
