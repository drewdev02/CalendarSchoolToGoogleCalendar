package excel.services.impl;

import excel.services.IExcelFileLoader;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Data
public class ExcelFileLoader implements IExcelFileLoader {
    private static final String FOLDER_PATH = "data/";

    @Override
    @SuppressWarnings("java:S112")
    public Workbook loadWorkbook() {
        var path = getFile();
        var file = new File(path);
        try (var workbook = WorkbookFactory.create(file)) {
            log.debug("Archivo cargado correctamente");
            return workbook;
        } catch (IOException e) {
            log.error("Error while reading the Excel file. {}", e.getMessage());
            throw new RuntimeException("Error while reading the Excel file.{}", e);
        }
    }

    private static List<File> getFileInFolder() {
        var folder = new File(FOLDER_PATH);
        if (folder.exists() && folder.isDirectory()) {
            return Stream.of(Objects.requireNonNull(folder.listFiles()))
                    .sorted(Comparator.comparingLong(File::lastModified).reversed())
                    .limit(1)
                    .toList();
        } else {
            log.error("Folder path does not exist or is not a directory");
            throw new IllegalArgumentException("Folder path does not exist or is not a directory");
        }
    }

    public static String getFile() {
        return getFileInFolder().get(0).getAbsolutePath();
    }

}
