package excel;


import excel.services.IDataExtractor;
import excel.services.impl.DataExtractor;
import excel.services.impl.ExcelFileLoader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import model.Subject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Data
@Builder
@AllArgsConstructor
public class ExcelReader {
    private IDataExtractor dataExtractor;

    public List<Subject> mapToListOfSubjects(@NotNull List<String> list, String day) {
        var listSubjet = new ArrayList<String>();
        var listLocation = new ArrayList<String>();
        IntStream.range(0, list.size() / 2)
                .forEach(i -> {
                    if (i % 2 == 0) {
                        listSubjet.add(list.get(i));
                    } else {
                        listLocation.add(list.get(i));
                    }
                });
        return IntStream.range(0, listSubjet.size())
                .mapToObj(i -> Subject.builder()
                        .day(day)
                        .name(listSubjet.get(i))
                        .location(listLocation.get(i))
                        .build())
                .toList();
    }

    public static void main(String[] args) {
        var excelReader = ExcelReader.builder()
                .dataExtractor(DataExtractor.builder()
                        .fileLoader(new ExcelFileLoader())
                        .build()
                )
                .build();
        var day = excelReader.getDataExtractor().extractDataFromCell("D21");

        var time = excelReader.getDataExtractor()
                .extractDataBelowCell("C21")
                .stream()
                .filter(s -> !s.equals("null"))
                .toList();

        var data = excelReader.getDataExtractor()
                .extractDataBelowCell("D21");

        var sub = excelReader.mapToListOfSubjects(data, day);

        System.out.println(time);
        System.out.println(data);
        System.out.println(sub);

    }


}
