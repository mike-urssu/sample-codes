package caffeLabel.captioning;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
    public Map<String, Category> getCategories(File excel) {
        Map<String, Category> categoryMap = new HashMap<>();
        try (
                FileInputStream inputStream = new FileInputStream(excel);
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream)
        ) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowIndices = sheet.getPhysicalNumberOfRows();
            for (int rowIndex = 1; rowIndex < rowIndices; rowIndex++) {
                XSSFRow row = sheet.getRow(rowIndex);
                if (row != null) {
                    String filename = row.getCell(0).getStringCellValue();
                    String folderName = row.getCell(1).getStringCellValue();
                    String superCategory = row.getCell(2).getStringCellValue();
                    String id = String.format("%03d", (int) row.getCell(3).getNumericCellValue());
                    String name = row.getCell(4).getStringCellValue();
                    Category category = new Category(filename, folderName, superCategory, id, name);

                    String object = filename.substring(filename.indexOf("("), filename.indexOf(")"));
                    categoryMap.put(object, category);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categoryMap;
    }
}