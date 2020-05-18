package util;

import model.Ad;
import model.Category;
import model.Gender;
import model.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class XLSXUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyy");

    public static List<Ad> readAds(String path) {
        List<Ad> result = new LinkedList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(path);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                result.add(getAdFromRow(row));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while importing ads.");
        }
        return result;
    }

    private static Ad getAdFromRow(Row row) {
        Cell phoneNumber = row.getCell(6);
        String phoneNumberStr = phoneNumber.getCellType() == CellType.NUMERIC ?
                String.valueOf(Double.valueOf(phoneNumber.getNumericCellValue()).intValue()) : phoneNumber.getStringCellValue();

        return Ad.builder()
                .id(Double.valueOf(row.getCell(0).getNumericCellValue()).intValue())
                .title(row.getCell(1).getStringCellValue())
                .text(row.getCell(2).getStringCellValue())
                .price(row.getCell(3).getNumericCellValue())
                .date(row.getCell(4).getDateCellValue())
                .category(Category.valueOf(row.getCell(5).getStringCellValue()))
                .author(User.builder().phoneNumber(phoneNumberStr).build())
                .build();
    }

    public static void writeAds(String path, List<Ad> ads) {
        String fileName = "Ads_Export_" + sdf.format(new Date()) + "_" + System.nanoTime() + ".xlsx";
        File file = new File(path, fileName);

        try {
            file.createNewFile();
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("exported ads");
            writeAdsHeader(sheet.createRow(0));
            int rowIndex = 1;
            CellStyle dateCellStyle = workbook.createCellStyle();
            short df = workbook.createDataFormat().getFormat("dd-MM-yyyy hh:mm:ss");
            dateCellStyle.setDataFormat(df);
            for (Ad ad : ads) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(ad.getId());
                row.createCell(1).setCellValue(ad.getTitle());
                row.createCell(2).setCellValue(ad.getText());
                row.createCell(3).setCellValue(ad.getPrice());
                Cell dateCell = row.createCell(4);
                dateCell.setCellValue(ad.getDate());
                dateCell.setCellStyle(dateCellStyle);
                row.createCell(5).setCellValue(ad.getCategory().name());
                row.createCell(6).setCellValue(ad.getAuthor().getPhoneNumber());
            }
            workbook.write(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while exporting items");
        }
    }

    private static void writeAdsHeader(Row row) {
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("title");
        row.createCell(2).setCellValue("text");
        row.createCell(3).setCellValue("price");
        row.createCell(4).setCellValue("date");
        row.createCell(5).setCellValue("category");
        row.createCell(6).setCellValue("author phone");
    }

    public static List<User> readUsers(String path) {
        List<User> result = new LinkedList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(path);
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                result.add(getUserFromRow(row));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while importing users.");
        }
        return result;
    }

    private static User getUserFromRow(Row row) {
        Cell phoneNumber = row.getCell(4);
        String phoneNumberStr = phoneNumber.getCellType() == CellType.NUMERIC ?
                String.valueOf(Double.valueOf(phoneNumber.getNumericCellValue()).intValue()) : phoneNumber.getStringCellValue();
        Cell password = row.getCell(5);
        String passwordStr = password.getCellType() == CellType.NUMERIC ?
                String.valueOf(Double.valueOf(password.getNumericCellValue()).intValue()) : password.getStringCellValue();

        return User.builder()
                .name(row.getCell(0).getStringCellValue())
                .surname(row.getCell(1).getStringCellValue())
                .age(Double.valueOf(row.getCell(2).getNumericCellValue()).intValue())
                .gender(Gender.valueOf(row.getCell(3).getStringCellValue()))
                .phoneNumber(phoneNumberStr)
                .password(passwordStr)
                .build();
    }


}
