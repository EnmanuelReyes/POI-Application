package me.enmanuel;

import me.enmanuel.util.DateUtil;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Enmanuel
 * Date: 09/09/2016
 * Time: 07:13 PM
 */
@Repository
public class ProductRepository {

    @Autowired
    ResourceLoader resourceLoader;

    String fileName = "database.xlsx";
    File database;

    @PostConstruct
    public void init() {
        Resource resource = new ClassPathResource(fileName);

        try {
            database = resource.getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        database = new File("C:\\Users\\Enmanuel\\Desktop\\" + fileName);

    }

    public void save(Product product) {
        XSSFWorkbook workbook = null;
        try (FileInputStream fis = new FileInputStream(database)) {
            workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i > 0; i++) {
                XSSFRow xssfRow = sheet.getRow(i);
                if (isRowEmpty(xssfRow)) {
                    xssfRow = sheet.createRow(i);
                    Cell cellId = xssfRow.createCell(0);
                    cellId.setCellValue(getLastId());
                    XSSFCell cellDate = xssfRow.createCell(1);
                    cellDate.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                    cellDate.setCellValue(DateUtil.toDate(product.getDate()));
                    Cell cellName = xssfRow.createCell(2);
                    cellName.setCellValue(product.getName());
                    Cell cellQuantity = xssfRow.createCell(3);
                    cellQuantity.setCellValue(product.getQuantity());
                    Cell cellPrice = xssfRow.createCell(4);
                    cellPrice.setCellValue(product.getPrice());
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (FileOutputStream fileOutputStream = new FileOutputStream(database)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Product get(Serializable id) {
        return getAll().stream().filter(x -> x.getId().equals(id)).findAny().orElse(null);
    }

    private Integer getLastId() {
        return getAll().stream().mapToInt(Product::getId).map(i -> -i).sorted().map(i -> -i).findFirst().orElse(0)+1;
    }

    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        XSSFWorkbook workbook = null;
        try (FileInputStream fis = new FileInputStream(database)) {
            workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i > 0; i++) {
                XSSFRow xssfRow = sheet.getRow(i);
                if (isRowEmpty(xssfRow)) {
                    break;
                } else {
                    Product p = ProductMapper.mapRow(xssfRow);
                    products.add(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static boolean isRowEmpty(XSSFRow row) {
        if (row == null) {
            return true;
        }
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            XSSFCell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != XSSFCell.CELL_TYPE_BLANK)
                return false;
        }
        return true;
    }

    public void update(Product product) {
        XSSFWorkbook workbook = null;
        try (FileInputStream fis = new FileInputStream(database)) {
            workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i > 0; i++) {
                XSSFRow xssfRow = sheet.getRow(i);
                if (isRowEmpty(xssfRow)) {
                    break;
                } else {
                    if (product.getId().equals((int) xssfRow.getCell(0).getNumericCellValue())) {
                        xssfRow.getCell(1).setCellValue(DateUtil.toDate(product.getDate()));
                        xssfRow.getCell(2).setCellValue(product.getName());
                        xssfRow.getCell(3).setCellValue(product.getQuantity());
                        xssfRow.getCell(4).setCellValue(product.getPrice());
                        break;

                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (FileOutputStream fileOutputStream = new FileOutputStream(database)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(Product product) {
        XSSFWorkbook workbook = null;
        try (FileInputStream fis = new FileInputStream(database)) {
            workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i > 0; i++) {
                XSSFRow xssfRow = sheet.getRow(i);
                if (isRowEmpty(xssfRow)) {
                    break;
                } else {
                    if (product.getId().equals((int) xssfRow.getCell(0).getNumericCellValue())) {
                        sheet.removeRow(xssfRow);
                        reorderRows(workbook, sheet);

                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (FileOutputStream fileOutputStream = new FileOutputStream(database)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reorderRows(XSSFWorkbook workbook, XSSFSheet sheet) {
        for (int i = 1; i > 0; i++) {
            XSSFRow xssfRow = sheet.getRow(i);
            if (isRowEmpty(xssfRow) && !isRowEmpty(sheet.getRow(i+1))) {
                copyRow(workbook, sheet, i+1, i);
                sheet.removeRow(sheet.getRow(i+1));
            } else if (isRowEmpty(xssfRow) && isRowEmpty(sheet.getRow(i+1))){
                return;
            }
        }
    }


    private static void copyRow(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        XSSFRow newRow = worksheet.getRow(destinationRowNum);
        XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            XSSFCell oldCell = sourceRow.getCell(i);
            XSSFCell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            XSSFCellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            ;
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }

        // If there are are any merged regions in the source row, copy to new row
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
            org.apache.poi.ss.util.CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() +
                                (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
                                )),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }

}
