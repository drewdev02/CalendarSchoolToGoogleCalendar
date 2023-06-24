package excel.services.impl;

import excel.services.IDataExtractor;
import excel.services.IExcelFileLoader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import util.Validation;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Data
@Builder
@AllArgsConstructor
public class DataExtractor implements IDataExtractor {

    private IExcelFileLoader fileLoader;

    private Workbook loadWorkbook() {
        return fileLoader.loadWorkbook();
    }

    @Override
    public List<String> getAllSheetNames() {
        var workbook = loadWorkbook();
        var numSheets = workbook.getNumberOfSheets();
        return IntStream.range(0, numSheets)
                .mapToObj(workbook::getSheetAt)
                .map(Sheet::getSheetName)
                .toList();
    }

    @Override
    public Sheet getSheetByName(String sheetName) {
        var workbook = loadWorkbook();
        Validation.validateNotBlank(sheetName, "Sheet name cannot be blank");

        var numSheets = workbook.getNumberOfSheets();
        Validation.validateIndexInRange(3, numSheets, "Sheet index is out of range");

        return workbook.getSheet(sheetName);
    }

    @Override
    public List<String> extractDataBelowCell(String cellReference) {
        Validation.validateNotBlank(cellReference, "Cell reference cannot be blank");

        var reference = new CellReference(cellReference);
        var sheetNames = getAllSheetNames();
        Validation.validateIndexInRange(3, sheetNames.size(), "Invalid sheet index");

        var sheet = getSheetByName(sheetNames.get(3));

        var columnNum = reference.getCol();
        var startRow = reference.getRow() + 1;
        var endRow = startRow + 11;

        return IntStream.rangeClosed(startRow, endRow)
                .mapToObj(sheet::getRow)
                .peek(row -> {
                    Validation.validateNotNull(row, "Row does not exist");
                    log.debug("Processing row: {}", row.getRowNum());
                })
                .map(row -> row.getCell(columnNum))
                .map(currentCell -> {
                    if (currentCell != null) {
                        switch (currentCell.getCellType()) {
                            case STRING -> {
                                return currentCell.getStringCellValue();
                            }
                            case NUMERIC -> {
                                return String.valueOf(currentCell.getNumericCellValue());
                            }
                            case BOOLEAN -> {
                                return String.valueOf(currentCell.getBooleanCellValue());
                            }
                            case FORMULA -> {
                                return currentCell.getCellFormula();
                            }
                            case BLANK -> {
                                return "null";
                            }
                            case ERROR -> {
                                return "Error en la celda";
                            }
                            default -> {
                                log.warn("Unsupported cell type: {}", currentCell.getCellType());
                                return "";
                            }
                        }
                    } else {
                        return "";
                    }
                })
                .toList();
    }
}
