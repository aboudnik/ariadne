package org.boudnik.ariadne.handlers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.boudnik.ariadne.DataBlock;
import org.boudnik.ariadne.DataFactory;
import org.boudnik.ariadne.DataSource;
import org.boudnik.ariadne.FieldsCache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Sergey Nuyanzin
 * @since 5/28/2018
 */
public class CsvFileHandler implements Handler {
    private static final char DEFAULT_DELIMITER = ',';
    private final char delimiter;
    private final DataSource dataSource;

    public CsvFileHandler(DataSource dataSource, char delimiter) {
        this.dataSource = dataSource;
        this.delimiter = delimiter;
    }

    public CsvFileHandler(DataSource dataSource) {
        this(dataSource, DEFAULT_DELIMITER);
    }

    @Override
    public Collection handle(DataBlock dataBlock) throws IOException, IllegalAccessException {
        Map<String, Object> dimensions = dataBlock.dimensions();
        InputStream inputStream = dataSource.openStream();
        List resultCollection = new ArrayList();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .withIgnoreHeaderCase()
                    .withTrim());
            if (!csvParser.getHeaderMap().keySet().containsAll(dimensions.keySet())) {
                throw new RuntimeException("Not all required columns are present " + csvParser.getHeaderMap().keySet());
            }

            for (CSVRecord csvRecord : csvParser) {
                Map<String, String> csvRecordMap = csvRecord.toMap();
             /*   if (!isSatisfied(csvRecordMap, dimensions)) {
                    continue;
                }*/
                Object record = dataBlock.record();
                for (Map.Entry<String, Field> name2FieldEntry :
                        FieldsCache.getInstance().getFieldsMap(dataBlock.record().getClass()).entrySet()) {
                    setFieldValue(record, name2FieldEntry,
                            name2FieldEntry.getValue().getType(), csvRecordMap.get(name2FieldEntry.getKey()));
                }
                resultCollection.add(record);
            }
        }
        return resultCollection;
    }

    private void setFieldValue(Object record, Map.Entry<String, Field> name2FieldEntry,
                               Class<?> fieldType, String fieldValue) throws IllegalAccessException {
        if (fieldType.isAssignableFrom(String.class)) {
            name2FieldEntry.getValue().set(record, fieldValue);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            name2FieldEntry.getValue().set(record, Integer.valueOf(fieldValue));
        } else if (fieldType.isAssignableFrom(Boolean.class)) {
            name2FieldEntry.getValue().set(record, Boolean.valueOf(fieldValue));
        } else if (fieldType.isAssignableFrom(Long.class)) {
            name2FieldEntry.getValue().set(record, Long.valueOf(fieldValue));
        } else if (fieldType.isAssignableFrom(Double.class)) {
            name2FieldEntry.getValue().set(record, Double.valueOf(fieldValue));
        } else if (fieldType.isAssignableFrom(Float.class)) {
            name2FieldEntry.getValue().set(record, Float.valueOf(fieldValue));
        } else if (fieldType.isAssignableFrom(Date.class)) {
            name2FieldEntry.getValue().set(record, new Date(fieldValue));
        }
    }

    private boolean isSatisfied(Map<String, String> csvRecordMap, Map<String, Object> dimensions) {
        for (Map.Entry<String, Object> dim : dimensions.entrySet()) {
            if (!Objects.equals(dim.getValue(), csvRecordMap.get(dim.getKey()))) {
                DataFactory.LOGGER.fine("failed for the KEY " + dim.getKey()
                        + " => expected value " + dim.getValue() + " vs csv value " + csvRecordMap.get(dim.getKey()));
                return false;
            }
        }
        return true;
    }
}
