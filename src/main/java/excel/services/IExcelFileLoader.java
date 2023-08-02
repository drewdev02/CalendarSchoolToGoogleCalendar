package excel.services;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    /**
     * Gets a list of all files within a given folder.
     *
     * @param folderPath absolute or relative path of the folder
     * @return a list with File objects representing each file found within the folder
     * @throws IllegalArgumentException if the folder path does not exist or is not a directory
     */
    List<File> getAllFilesInFolder(String folderPath);
}