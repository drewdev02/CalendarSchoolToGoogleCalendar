package excel.services;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * Interface for extracting data from an Excel file.
 * Defines methods to access and read the contents
 * of a workbook and its sheets.
 */
public interface IDataExtractor {

    /**
     * Gets the names of all sheets in the workbook.
     *
     * @return a list with the sheet names
     */
    List<String> getAllSheetNames();

    /**
     * Gets a specific sheet by its name.
     *
     * @param sheetName the name of the sheet to retrieve
     * @return the sheet that corresponds to the given name
     * @throws IllegalArgumentException if name is empty
     */
    Sheet getSheetByName(String sheetName);

    /**
     * Extracts cell data from a column under
     * a reference cell.
     *
     * @param cellReference the reference cell
     * @return list of strings with extracted data
     */
    List<String> extractDataBelowCell(String cellReference);

    /**
     * Extracts the data contained in a given cell.
     *
     * @param cellReference the reference of the cell to read
     * @return the cell value as string
     */
    String extractDataFromCell(String cellReference);

}
