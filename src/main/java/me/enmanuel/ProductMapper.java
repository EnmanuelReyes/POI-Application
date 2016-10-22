package me.enmanuel;

import me.enmanuel.util.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * Created by IntelliJ IDEA.
 * User: Enmanuel
 * Date: 27/07/2016
 * Time: 05:36 PM
 */
public class ProductMapper {

    public static Product mapRow(XSSFRow row) {
        Product p = new Product();
        p.setId((int) row.getCell(0).getNumericCellValue());
        p.setDate(DateUtil.toLocalDate(row.getCell(1).getDateCellValue()));
        p.setName(getString(row.getCell(2)));
        p.setQuantity(row.getCell(3).getNumericCellValue());
        p.setPrice(row.getCell(4).getNumericCellValue());

        return p;
    }


    public static String getString(XSSFCell hssfCell) {
        if (hssfCell == null) return null;
        try {
            if (hssfCell.getStringCellValue() != null)
                return hssfCell.getStringCellValue();
        } catch (IllegalStateException e) {
            if (!Double.isNaN(hssfCell.getNumericCellValue()))
                return String.valueOf(hssfCell.getNumericCellValue());
        }


        return null;
    }
    }
