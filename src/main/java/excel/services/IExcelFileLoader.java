package excel.services;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Interface for loading an Excel workbook.
 */
public interface IExcelFileLoader {

    /**
     * Loads the Excel workbook from a given path.
     *
     * @return the Workbook object representing the loaded workbook
     * @throws RuntimeException if error while reading the Excel file
     */
    Workbook loadWorkbook();

}