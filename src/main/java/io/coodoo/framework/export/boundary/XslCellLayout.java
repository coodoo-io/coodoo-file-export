package io.coodoo.framework.export.boundary;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XslCellLayout {

    public boolean wraptext;
    public String color;
    public String alignment;
    public String verticalAlignment;
    public int heigth;
    public String fontName;
    public int fontSize;
    public boolean fontBold;
    public boolean fontItalic;
    public String fontColor;

    public XslCellLayout(boolean wraptext, String color, String alignment, String verticalAlignment, int heigth, String fontName, int fontSize,
                    boolean fontBold, boolean fontItalic, String fontColor) {
        super();
        this.wraptext = wraptext;
        this.color = color;
        this.alignment = alignment;
        this.verticalAlignment = verticalAlignment;
        this.heigth = heigth;
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.fontBold = fontBold;
        this.fontItalic = fontItalic;
        this.fontColor = fontColor;
    }

    public XSSFCellStyle cellStyle(XSSFWorkbook workbook) {

        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setFillForegroundColor(colorOfHex(color));
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setWrapText(wraptext);

        switch (alignment) {
            case "LEFT":
                style.setAlignment(CellStyle.ALIGN_LEFT);
                break;
            case "CENTER":
                style.setAlignment(CellStyle.ALIGN_CENTER);
                break;
            case "RIGHT":
                style.setAlignment(CellStyle.ALIGN_RIGHT);
                break;
            default:
                style.setAlignment(CellStyle.ALIGN_JUSTIFY);
                break;
        }
        switch (verticalAlignment) {
            case "TOP":
                style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
                break;
            case "CENTER":
                style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                break;
            case "BOTTOM)":
                style.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
                break;
            default:
                style.setVerticalAlignment(CellStyle.VERTICAL_JUSTIFY);
                break;
        }

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) fontSize);
        font.setFontName(fontName);
        font.setBold(fontBold);
        font.setItalic(fontItalic);
        try {
            font.setColor(IndexedColors.valueOf(fontColor).getIndex());
        } catch (IllegalArgumentException e) {
            font.setColor(IndexedColors.BLACK.getIndex());
        }
        style.setFont(font);

        return style;
    }

    public static XSSFColor colorOfHex(String hexColor) {

        Matcher hexColorMatcher = Pattern.compile("^#?([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})$").matcher(hexColor);
        if (hexColorMatcher.find()) {

            Integer red = Integer.valueOf(hexColorMatcher.group(1), 16);
            Integer green = Integer.valueOf(hexColorMatcher.group(2), 16);
            Integer blue = Integer.valueOf(hexColorMatcher.group(3), 16);

            Color color = new Color(red, green, blue);
            return new XSSFColor(color);
        }
        return null;
    }

    public boolean isWraptext() {
        return wraptext;
    }

    public void setWraptext(boolean wraptext) {
        this.wraptext = wraptext;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(String verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isFontBold() {
        return fontBold;
    }

    public void setFontBold(boolean fontBold) {
        this.fontBold = fontBold;
    }

    public boolean isFontItalic() {
        return fontItalic;
    }

    public void setFontItalic(boolean fontItalic) {
        this.fontItalic = fontItalic;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

}
