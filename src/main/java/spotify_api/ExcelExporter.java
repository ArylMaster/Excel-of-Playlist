package spotify_api;

import java.io.FileOutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.logging.Logger;

public class ExcelExporter {
    private static final Logger LOGGER = Logger.getLogger(ExcelExporter.class.getName());

    public void export(List<Track> tracks, String fileName) {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream out = new FileOutputStream(fileName)) {
            Sheet sheet = workbook.createSheet("Tracks");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Track Name");
            header.createCell(1).setCellValue("Artist");
            header.createCell(2).setCellValue("Album");

            int rowNum = 1;
            for (Track track : tracks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(track.name);
                row.createCell(1).setCellValue(track.artist);
            workbook.write(out);
            if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
                LOGGER.info(String.format("Excel file written: %s", fileName));
            }
        }
    } catch (Exception e) {
            LOGGER.severe("Error exporting to Excel: " + e.getMessage());
        }
}
}
