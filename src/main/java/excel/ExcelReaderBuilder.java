package excel;


import excel.services.IDataExtractor;
import excel.services.IEventMapper;
import excel.services.IExcelFileLoader;
import excel.services.impl.DataExtractor;
import excel.services.impl.EventMapper;
import excel.services.impl.ExcelFileLoader;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class ExcelReaderBuilder {
    private IExcelFileLoader fileLoader;
    private IDataExtractor dataExtractor;
    private IEventMapper eventMapper;

    public ExcelReaderBuilder() {
        log.debug("Creating new ExcelReaderBuilder instance");
    }

    public ExcelReaderBuilder withExcelFileLoader() {
        this.fileLoader = new ExcelFileLoader();
        log.debug("ExcelFileLoader set");
        return this;
    }

    public ExcelReaderBuilder withDataExtractor() {
        this.dataExtractor = DataExtractor.builder()
                .fileLoader(fileLoader)
                .build();
        log.debug("DataExtractor set");
        return this;
    }

    public ExcelReaderBuilder withEventMapper() {
        this.eventMapper = EventMapper.builder()
                .dataExtractor(dataExtractor)
                .build();
        log.debug("EventMapper set");
        return this;
    }

    public ExcelReaderBuilder build() {
        if (fileLoader == null) {
            throw new IllegalStateException("ExcelFileLoader is not set. Call withExcelFileLoader() before calling build().");
        }
        if (dataExtractor == null) {
            throw new IllegalStateException("DataExtractor is not set. Call withDataExtractor() before calling build().");
        }
        if (eventMapper == null) {
            throw new IllegalStateException("EventMapper is not set. Call withEventMapper() before calling build().");
        }
        log.debug("Building ExcelReaderBuilder");
        return this;
    }
}
