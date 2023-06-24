package excel;


import excel.services.IDataExtractor;
import excel.services.impl.DataExtractor;
import excel.services.impl.ExcelFileLoader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import util.HoraUtil;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Builder
@AllArgsConstructor
public class ExcelReader {
    private IDataExtractor dataExtractor;

    public static <T> List<T> interleaveLists(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>();

        int size = Math.max(list1.size(), list2.size());

        for (int i = 0; i < size; i++) {
            if (i < list1.size()) {
                result.add(list1.get(i));
            }
            if (i < list2.size()) {
                result.add(list2.get(i));
            }
        }

        return result;
    }

    public void readExcel() {
        var sheetNames = dataExtractor.getAllSheetNames();
        log.info("Sheet names: {}", sheetNames);

        var sheet = dataExtractor.getSheetByName(sheetNames.get(3));
        log.info("Sheet: {}", sheet);

        var data = dataExtractor.extractDataBelowCell("C21");
        log.info("Data: {}", data);
    }

    public static void main(String[] args) {
        var excelReader = ExcelReader.builder()
                .dataExtractor(DataExtractor.builder()
                        .fileLoader(new ExcelFileLoader())
                        .build()
                )
                .build();

        var data = excelReader.getDataExtractor().extractDataBelowCell("C21");
        var datamod = data.stream().filter(s -> !s.equalsIgnoreCase("null"))
                .map(s -> s.split("-")[0].trim())
                .toList();
        var datamod2 = data.stream().filter(s -> !s.equalsIgnoreCase("null"))
                .map(s -> s.split("-")[1].trim())
                .toList();
        //unit datamod y datamod2 intercalando elementos uno de datamod y otro de datamod2
        var datamod3 = interleaveLists(datamod, datamod2);


        var lista = HoraUtil.algo("22/06/2023", datamod3);
        System.out.println(data);
        System.out.println(datamod);
        System.out.println(lista);
        System.out.println(datamod3);

    }


}
