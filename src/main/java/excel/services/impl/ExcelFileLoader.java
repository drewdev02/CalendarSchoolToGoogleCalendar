package excel.services.impl;

import excel.services.IExcelFileLoader;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;

@Slf4j
@NoArgsConstructor
public class ExcelFileLoader implements IExcelFileLoader {
    private static final String DATA_FILE_PATH = "data/Horario_FTE_CRD_190623-240623.xlsx";

    @Override
    public Workbook loadWorkbook() {
        try (var workbook = WorkbookFactory.create(new File(DATA_FILE_PATH))) {
            log.debug("Archivo cargado correctamente");
            return workbook;
        } catch (IOException e) {
            log.error("Error while reading the Excel file.", e);
            throw new RuntimeException("Error while reading the Excel file.", e);
        }
    }
}
