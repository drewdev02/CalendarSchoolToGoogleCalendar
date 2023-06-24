package excel.services;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface IDataExtractor {
    List<String> getAllSheetNames();

    Sheet getSheetByName(String sheetName);

    List<String> extractDataBelowCell(String cellReference);
}
