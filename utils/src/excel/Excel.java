package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
    public static void read(File excel) {
        try (FileInputStream inputStream = new FileInputStream(excel); XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowIndices = sheet.getPhysicalNumberOfRows();
            for (int rowIndex = 0; rowIndex < rowIndices; rowIndex++) {
                XSSFRow row = sheet.getRow(rowIndex);
                if (row != null) {
                    int columnIndices = row.getPhysicalNumberOfCells();
                    for (int columnIndex = 0; columnIndex <= columnIndices; columnIndex++) {
                        XSSFCell cell = row.getCell(columnIndex);
                        String value = "";
                        if (cell == null) {
                            continue;
                        } else {
                            switch (cell.getCellType()) {
                                case FORMULA:
                                    value = cell.getCellFormula();
                                    break;
                                case NUMERIC:
                                    value = cell.getNumericCellValue() + "";
                                    break;
                                case STRING:
                                    value = cell.getStringCellValue() + "";
                                    break;
                                case BLANK:
                                    value = cell.getBooleanCellValue() + "";
                                    break;
                                case ERROR:
                                    value = cell.getErrorCellValue() + "";
                                    break;
                            }
                        }
                        System.out.println(rowIndex + "번 행 : " + columnIndex + "번 열 값은: " + value);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
