package excel.services.impl;

import excel.services.IExcelFileLoader;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Data
public class ExcelFileLoader implements IExcelFileLoader {
    public static final String DATA_FILE_PATH = "data/Horario_FTE_CRD_201123-241123.xls";
    //public static final String DATA_FILE_PATH = getAllFilesInFolder().get(0).getAbsolutePath();

    @Override
    @SuppressWarnings("java:S112")
    public Workbook loadWorkbook() {
        var file = new File(DATA_FILE_PATH);
        try (var workbook = WorkbookFactory.create(file)) {
            log.debug("Archivo cargado correctamente");
            return workbook;
        } catch (IOException e) {
            log.error("Error while reading the Excel file. {}", e.getMessage());
            throw new RuntimeException("Error while reading the Excel file.{}", e);
        }
    }

    public static List<File> getAllFilesInFolder() {
        var FOLDER_PATH = "data/";
        var folder = new File(FOLDER_PATH);
        if (folder.exists() && folder.isDirectory()) {
            return Stream.of(Objects.requireNonNull(folder.listFiles()))
                    .toList();
        } else {
            log.error("Folder path does not exist or is not a directory");
            throw new IllegalArgumentException("Folder path does not exist or is not a directory");
        }

    }

}
