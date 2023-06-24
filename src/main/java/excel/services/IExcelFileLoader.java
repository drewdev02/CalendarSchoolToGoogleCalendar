package excel.services;

import org.apache.poi.ss.usermodel.Workbook;

public interface IExcelFileLoader {
    Workbook loadWorkbook();
}
