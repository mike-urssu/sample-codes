package caffeLabel.captioning;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
    public Map<String, String> getCategoryMap(File excel) {
        Map<String, String> categoryMap = new HashMap<>();
        try (
                FileInputStream inputStream = new FileInputStream(excel);
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream)
        ) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowIndices = sheet.getPhysicalNumberOfRows();
            for (int rowIndex = 0; rowIndex < rowIndices; rowIndex++) {
                XSSFRow row = sheet.getRow(rowIndex);
                if (row != null) {
                    String object = FilenameUtils.getBaseName(row.getCell(0).getStringCellValue());
                    String instance = row.getCell(1).getStringCellValue();
                    categoryMap.put(object, instance);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categoryMap;
    }
}

