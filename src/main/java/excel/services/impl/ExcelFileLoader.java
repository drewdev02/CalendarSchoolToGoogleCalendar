package excel.services.impl;

import excel.services.IExcelFileLoader;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor
public class ExcelFileLoader implements IExcelFileLoader {
    public static final String DATA_FILE_PATH = "data/Horario_FTE_CRD_190623-240623.xlsx";

    @Override
    public Workbook loadWorkbook() {
        try (var workbook = WorkbookFactory.create(new File(DATA_FILE_PATH))) {
            log.debug("Archivo cargado correctamente");
            return workbook;
        } catch (IOException e) {
            log.error("Error while reading the Excel file. {}", e.getMessage());
            throw new RuntimeException("Error while reading the Excel file.{}", e);
        }
    }

    @Override
    public List<File> getAllFilesInFolder(String folderPath) {
        var folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            return Stream.of(Objects.requireNonNull(folder.listFiles()))
                    .toList();
        } else {
            log.error("Folder path does not exist or is not a directory");
            throw new IllegalArgumentException("Folder path does not exist or is not a directory");
        }

    }

}
