package excel;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class Excel {
    public void download() throws IOException {
        try (
            Workbook workbook = new SXSSFWorkbook();
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("utils/files/excel/excel.xlsx"))
        ) {
            Sheet sheet = workbook.createSheet();

            CellStyle greyCellStyle = workbook.createCellStyle();
            applyCellStyle(greyCellStyle, new Color(231, 234, 236));

            CellStyle blueCellStyle = workbook.createCellStyle();
            applyCellStyle(blueCellStyle, new Color(223, 235, 246));

            CellStyle bodyCellStyle = workbook.createCellStyle();
            applyCellStyle(bodyCellStyle, new Color(255, 255, 255));

            // header
            int rowIndex = 0;
            Row headerRow = sheet.createRow(rowIndex++);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("회사");
            headerCell1.setCellStyle(greyCellStyle);

            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("차종");
            headerCell2.setCellStyle(greyCellStyle);

            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("가격");
            headerCell3.setCellStyle(blueCellStyle);

            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("평점");
            headerCell4.setCellStyle(blueCellStyle);

            // content
            List<CarExcelDto> cars = getCars();
            for (CarExcelDto car : cars) {
                Row bodyRow = sheet.createRow(rowIndex++);

                Cell bodyCell1 = bodyRow.createCell(0);
                bodyCell1.setCellValue(car.getCompany());
                bodyCell1.setCellStyle(bodyCellStyle);

                Cell bodyCell2 = bodyRow.createCell(1);
                bodyCell2.setCellValue(car.getName());
                bodyCell2.setCellStyle(bodyCellStyle);

                Cell bodyCell3 = bodyRow.createCell(2);
                bodyCell3.setCellValue(car.getPrice());
                bodyCell3.setCellStyle(bodyCellStyle);

                Cell bodyCell4 = bodyRow.createCell(3);
                bodyCell4.setCellValue(car.getRating());
                bodyCell4.setCellStyle(bodyCellStyle);
            }
            workbook.write(outputStream);
        }
    }

    private List<CarExcelDto> getCars() {
        return Arrays.asList(
            new CarExcelDto("현대", "소나타", 100, 4.9),
            new CarExcelDto("르노삼성", "QM6", 200, 4.9),
            new CarExcelDto("기아", "K7", 300, 4.9)
        );
    }

    private void applyCellStyle(CellStyle cellStyle, Color color) {
        XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
        xssfCellStyle.setFillForegroundColor(new XSSFColor(color, new DefaultIndexedColorMap()));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
    }

    public static void main(String[] args) throws IOException {
        Excel excel = new Excel();
        excel.download();
    }
}

class CarExcelDto {
    private final String company;
    private final String name;
    private final int price;
    private final double rating;

    public CarExcelDto(String company, String name, int price, double rating) {
        this.company = company;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    public String getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }
}
