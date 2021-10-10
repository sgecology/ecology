/**
 * 
 */
package net.ecology.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import lombok.Builder;
import net.ecology.domain.Context;

/**
 * @author ducbq
 *
 */
@Builder
public class CSVUtilityHelper {
  public List<String[]> fetchData(InputStream inputStream, char separator, int skipLines) throws IOException, CsvException{
    CSVParser customCsvParser = new CSVParserBuilder()
        .withSeparator(separator)
        .build(); // custom separator
    CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream))
        .withCSVParser(customCsvParser)   // custom CSV parser
        .withSkipLines(skipLines)           // skip the first line, header info
        .build();

    return reader.readAll();
  }

  /*public Context fetchCsvData(Context ctx) throws IOException, CsvException{
  	IOContainer ioDataContainer = (IOContainer)ctx.get(CommonConstants.IO_DATA_CONTAINER);
  	char separator = (char)ctx.get(CommonConstants.SEPARATOR);
  	int skipLines = (int)ctx.get(CommonConstants.SKIP_LINES);

  	CSVParser customCsvParser = new CSVParserBuilder()
        .withSeparator(separator)
        .build(); // custom separator
    CSVReader reader = new CSVReaderBuilder(new InputStreamReader(ioDataContainer.getInStream()))
        .withCSVParser(customCsvParser)   // custom CSV parser
        .withSkipLines(skipLines)           // skip the first line, header info
        .build();

    return ctx.put(reader.readAll());
  }*/

  public List<String[]> fetchData(Context ioContainer, char separator, int skipLines) throws IOException, CsvException{
    CSVParser customCsvParser = new CSVParserBuilder()
        .withSeparator(separator)
        .build(); // custom separator
    CSVReader reader = new CSVReaderBuilder(new InputStreamReader((InputStream)ioContainer.get(Context.DEFAULT)))
        .withCSVParser(customCsvParser)   // custom CSV parser
        .withSkipLines(skipLines)           // skip the first line, header info
        .build();

    return reader.readAll();
  }
}
