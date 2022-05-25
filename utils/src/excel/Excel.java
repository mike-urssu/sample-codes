package excel;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class Excel {
    public void download() throws IOException {
        try (
            Workbook workbook = new SXSSFWorkbook();
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("utils/files/excel/excel.xlsx"))
        ) {
            Sheet sheet = workbook.createSheet();

            // header
            int rowIndex = 0;
            Row headerRow = sheet.createRow(rowIndex++);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("회사");

            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("차종");

            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("가격");

            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("평점");

            // content
            List<CarExcelDto> cars = getCars();
            for (CarExcelDto car : cars) {
                Row bodyRow = sheet.createRow(rowIndex++);

                Cell bodyCell1 = bodyRow.createCell(0);
                bodyCell1.setCellValue(car.getCompany());

                Cell bodyCell2 = bodyRow.createCell(1);
                bodyCell2.setCellValue(car.getName());

                Cell bodyCell3 = bodyRow.createCell(2);
                bodyCell3.setCellValue(car.getPrice());

                Cell bodyCell4 = bodyRow.createCell(3);
                bodyCell4.setCellValue(car.getRating());
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
