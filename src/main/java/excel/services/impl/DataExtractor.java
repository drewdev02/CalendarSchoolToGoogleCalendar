package excel.services.impl;

import excel.services.IDataExtractor;
import excel.services.IExcelFileLoader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
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
    private Workbook workbookInstance;

    private Workbook loadWorkbook() {
        // Si no se ha cargado el workbook previamente, se carga utilizando el fileLoader
        if (workbookInstance == null) {
            workbookInstance = fileLoader.loadWorkbook();
        }
        return workbookInstance;
    }

    @Override
    public List<String> getAllSheetNames() {
        // Obtiene el workbook y el número de hojas
        var workbook = loadWorkbook();
        var numSheets = workbook.getNumberOfSheets();

        // Obtiene los nombres de todas las hojas utilizando IntStream y mapToObj
        return IntStream.range(0, numSheets)
                .mapToObj(workbook::getSheetAt)
                .map(Sheet::getSheetName)
                .toList();
    }

    @Override
    public Sheet getSheetByName(String sheetName) {
        // Obtiene el workbook
        var workbook = loadWorkbook();
        Validation.validateNotBlank(sheetName, "Sheet name cannot be blank");

        // Obtiene el número de hojas y valida el índice
        var numSheets = workbook.getNumberOfSheets();
        Validation.validateIndexInRange(3, numSheets, "Sheet index is out of range");

        // Obtiene la hoja por nombre
        return workbook.getSheet(sheetName);
    }

    private String getCellAsString(Cell cell) {
        if (cell != null) {
            // Obtiene el valor de la celda en formato de cadena según su tipo
            switch (cell.getCellType()) {
                case STRING -> {
                    return cell.getStringCellValue();
                }
                case NUMERIC -> {
                    return String.valueOf(cell.getNumericCellValue());
                }
                case BOOLEAN -> {
                    return String.valueOf(cell.getBooleanCellValue());
                }
                case FORMULA -> {
                    return cell.getCellFormula();
                }
                case BLANK -> {
                    return "null";
                }
                case ERROR -> {
                    log.warn("Error en la celda");
                    return "Error en la celda";
                }
                default -> {
                    log.warn("Unsupported cell type: {}", cell.getCellType());
                    return "";
                }
            }
        } else {
            log.warn("Cell is null: {}", cell.getCellType());
            return "Cell is null";
        }
    }

    @Override
    public List<String> extractDataBelowCell(String cellReference) {
        // Valida que la referencia de la celda no esté en blanco
        Validation.validateNotBlank(cellReference, "Cell reference cannot be blank");

        // Obtiene la referencia de la celda
        var reference = new CellReference(cellReference);

        // Obtiene los nombres de las hojas
        var sheetNames = getAllSheetNames();

        // Valida el índice de la hoja
        Validation.validateIndexInRange(3, sheetNames.size(), "Invalid sheet index");

        // Obtiene la hoja por nombre
        var sheet = getSheetByName(sheetNames.get(3));

        // Obtiene el número de columna y el rango de filas
        var columnNum = reference.getCol();
        var startRow = reference.getRow() + 1;
        var endRow = startRow + 11;

        // Obtiene los datos de las celdas dentro del rango especificado
        return IntStream.rangeClosed(startRow, endRow)
                .mapToObj(sheet::getRow)
                .peek(row -> {
                    // Valida que la fila exista
                    Validation.validateNotNull(row, "Row does not exist");
                    log.debug("Processing row: {}", row.getRowNum());
                })
                .map(row -> row.getCell(columnNum))
                .map(this::getCellAsString)
                .toList();
    }

    @Override
    public String extractDataFromCell(String cellReference) {
        // Valida que la referencia de la celda no esté en blanco
        Validation.validateNotBlank(cellReference, "Cell reference cannot be blank");

        // Obtiene la referencia de la celda
        var reference = new CellReference(cellReference);

        // Obtiene los nombres de las hojas
        var sheetNames = getAllSheetNames();

        // Valida el índice de la hoja
        Validation.validateIndexInRange(3, sheetNames.size(), "Invalid sheet index");

        // Obtiene la hoja por nombre
        var sheet = getSheetByName(sheetNames.get(3));

        // Obtiene el número de fila y columna
        var row = reference.getRow();
        var column = reference.getCol();

        // Obtiene la celda específica
        var cell = sheet.getRow(row).getCell(column);

        // Registra un mensaje de depuración con la referencia de la celda
        log.debug("Extracting data from cell: {}", cellReference);

        // Retorna el valor de la celda como una cadena
        return getCellAsString(cell);
    }

}

