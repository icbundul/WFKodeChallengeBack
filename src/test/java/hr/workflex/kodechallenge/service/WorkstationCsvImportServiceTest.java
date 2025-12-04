package hr.workflex.kodechallenge.service;

import hr.workflex.kodechallenge.domain.Workstation;
import hr.workflex.kodechallenge.repository.WorkstationRepository;
import hr.workflex.kodechallenge.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class WorkstationCsvImportServiceTest {

    @Mock
    private WorkstationRepository workstationRepository;

    private WorkstationCsvImportService service;

    @BeforeEach
    void setUp() {
        service = new WorkstationCsvImportService(workstationRepository);
    }

    @Test
    void importCsvData_parsesAndSaves_allRows() throws Exception {
        // Arrange: CSV with valid dates
        String csv =
                "workationId,employee,origin,destination,start,end,risk\n" +
                        "W1,Steffen Jacobs,Germany,United States,2024-10-02,2024-12-31,HIGH\n" +
                        "W2,Ayushi Singh,Germany,India,2023-03-13,2023-04-30,NO\n";
        String resourcePath = writeTestResource("workstations_ok.csv", csv);

        // Mock DateUtil.getWorkingDays(start, end) to return a predictable number
        try (MockedStatic<DateUtil> mocked = Mockito.mockStatic(DateUtil.class)) {
            mocked.when(() -> DateUtil.getWorkingDays(any(), any())).thenReturn(10);

            // Act
            service.importCsvData(resourcePath);

            // Assert
            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<Workstation>> captor = ArgumentCaptor.forClass(List.class);
            verify(workstationRepository, times(1)).saveAll(captor.capture());

            List<Workstation> saved = captor.getValue();
            assertEquals(2, saved.size());

            Workstation w1 = saved.get(0);
            assertEquals("W1", w1.getWorkstationId());  // note: mapped from "workationId"
            assertEquals("Steffen Jacobs", w1.getEmployee());
            assertEquals("Germany", w1.getOrigin());
            assertEquals("United States", w1.getDestination());
            assertNotNull(w1.getStart());
            assertNotNull(w1.getEnd());
            assertEquals("HIGH", w1.getRisk());
            assertEquals(10, w1.getWorkingDays());      // from mocked DateUtil

            Workstation w2 = saved.get(1);
            assertEquals("W2", w2.getWorkstationId());
            assertEquals("NO", w2.getRisk());
            assertEquals(10, w2.getWorkingDays());
        }
    }

    @Test
    void importCsvData_throwsWhenFileMissing() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> service.importCsvData("does/not/exist.csv"));
        assertTrue(ex.getMessage().contains("File not found"));
        verifyNoInteractions(workstationRepository);
    }

    @Test
    void importCsvData_invalidDates_areParsedAsNull_andSaved() throws Exception {
        // Arrange: one row has invalid date strings
        String csv =
                "workationId,employee,origin,destination,start,end,risk\n" +
                        "W3,Henry Duchamp,Belgium,Spain,bad-date,2023-03-01,HIGH\n" +
                        "W4,Andre Fischer,Germany,Greece,2023-04-22,bad-date,LOW\n";
        String resourcePath = writeTestResource("workstations_bad_dates.csv", csv);

        try (MockedStatic<DateUtil> mocked = Mockito.mockStatic(DateUtil.class)) {
            // Whatever the inputs (even null), return 0 working days so assertion is stable
            mocked.when(() -> DateUtil.getWorkingDays(any(), any())).thenReturn(0);

            // Act
            service.importCsvData(resourcePath);

            // Assert
            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<Workstation>> captor = ArgumentCaptor.forClass(List.class);
            verify(workstationRepository).saveAll(captor.capture());

            List<Workstation> saved = captor.getValue();
            assertEquals(2, saved.size());

            Workstation w3 = saved.get(0);
            assertNull(w3.getStart(), "Invalid start should parse to null");
            assertNotNull(w3.getEnd(), "Valid end should parse");
            assertEquals(0, w3.getWorkingDays());

            Workstation w4 = saved.get(1);
            assertNotNull(w4.getStart());
            assertNull(w4.getEnd(), "Invalid end should parse to null");
            assertEquals(0, w4.getWorkingDays());
        }
    }

    private String writeTestResource(String fileName, String content) throws Exception {
        URL rootUrl = getClass().getClassLoader().getResource("");
        assertNotNull(rootUrl, "Test classpath root should exist");

        Path root = Paths.get(rootUrl.toURI());

        Files.createDirectories(root);
        Path file = root.resolve(fileName);
        Files.write(file, content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return fileName;
    }
}
