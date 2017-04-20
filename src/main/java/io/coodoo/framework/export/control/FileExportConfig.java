package io.coodoo.framework.export.control;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Export configuration
 * 
 * @author coodoo GmbH (coodoo.io)
 */
@ApplicationScoped
public class FileExportConfig {

    private static Logger log = LoggerFactory.getLogger(FileExportConfig.class);

    public static int XSL_HEIGTH = 500;
    public static String XSL_FONT_NAME = "Helvetica";
    public static int XSL_FONT_SIZE = 9;
    public static boolean XSL_FONT_BOLD = false;
    public static boolean XSL_FONT_ITALIC = false;
    public static String XSL_FONT_COLOR = "BLACK";
    public static boolean XSL_STYLE_WRAPTEXT = true;
    public static String XSL_STYLE_COLOR = "#FFFFFF";
    public static String XSL_STYLE_COLOR_EVEN = "#ECECEC";
    public static String XSL_STYLE_COLOR_ODD = "#FFFFFF";
    public static String XSL_STYLE_ALIGNMENT = "LEFT";
    public static String XSL_STYLE_VERTICALALIGNMENT = "CENTER";

    public static int XSL_HEADER_HEIGTH = 600;
    public static String XSL_HEADER_FONT_NAME = "Helvetica";
    public static int XSL_HEADER_FONT_SIZE = 11;
    public static boolean XSL_HEADER_FONT_BOLD = true;
    public static boolean XSL_HEADER_FONT_ITALIC = false;
    public static String XSL_HEADER_FONT_COLOR = "WHITE";
    public static boolean XSL_HEADER_STYLE_WRAPTEXT = true;
    public static String XSL_HEADER_STYLE_COLOR = "#000099";
    public static String XSL_HEADER_STYLE_ALIGNMENT = "CENTER";
    public static String XSL_HEADER_STYLE_VERTICALALIGNMENT = "CENTER";

    /**
     * Timestamp pattern for export files
     */
    public static String TIMESTAMP_PATTERN = "yyyyMMddHHmmss";

    // keep 1000 rows in memory, exceeding rows will be flushed to disk
    public static int XSL_BUFFER_LIMIT = 1000;

    public static String CSV_SEPARATOR = ";";
    public static String CSV_QUOTES = "\"";

    /**
     * Name of the (optional) file export property file
     */
    private static final String fileexportPropertiesFilename = "coodoo.fileexport.properties";

    static Properties properties = new Properties();

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        FileExportConfig.loadConfig();
    }

    public static void loadConfig() {
        InputStream inputStream = null;
        try {
            inputStream = FileExportConfig.class.getClassLoader().getResourceAsStream(fileexportPropertiesFilename);

            if (inputStream != null) {

                properties.load(inputStream);
                log.info("Reading {}", fileexportPropertiesFilename);

                XSL_HEIGTH = loadProperty(XSL_HEIGTH, "coodoo.fileexport.xls.heigth");
                XSL_FONT_NAME = loadProperty(XSL_FONT_NAME, "coodoo.fileexport.xls.font.name");
                XSL_FONT_SIZE = loadProperty(XSL_FONT_SIZE, "coodoo.fileexport.xls.font.size");
                XSL_FONT_BOLD = loadProperty(XSL_FONT_BOLD, "coodoo.fileexport.xls.font.bold");
                XSL_FONT_ITALIC = loadProperty(XSL_FONT_ITALIC, "coodoo.fileexport.xls.font.italic");
                XSL_FONT_COLOR = loadProperty(XSL_FONT_COLOR, "coodoo.fileexport.xls.font.color");
                XSL_STYLE_WRAPTEXT = loadProperty(XSL_STYLE_WRAPTEXT, "coodoo.fileexport.xls.style.wraptext");
                XSL_STYLE_COLOR = loadProperty(XSL_STYLE_COLOR, "coodoo.fileexport.xls.style.color");
                XSL_STYLE_COLOR_EVEN = loadProperty(XSL_STYLE_COLOR_EVEN, "coodoo.fileexport.xls.style.color.even");
                XSL_STYLE_COLOR_ODD = loadProperty(XSL_STYLE_COLOR_ODD, "coodoo.fileexport.xls.style.color.odd");
                XSL_STYLE_ALIGNMENT = loadProperty(XSL_STYLE_ALIGNMENT, "coodoo.fileexport.xls.style.alignment");
                XSL_STYLE_VERTICALALIGNMENT = loadProperty(XSL_STYLE_VERTICALALIGNMENT, "coodoo.fileexport.xls.style.verticalalignment");

                XSL_HEADER_HEIGTH = loadProperty(XSL_HEADER_HEIGTH, "coodoo.fileexport.xls.heigth");
                XSL_HEADER_FONT_NAME = loadProperty(XSL_HEADER_FONT_NAME, "coodoo.fileexport.xls.font.name");
                XSL_HEADER_FONT_SIZE = loadProperty(XSL_HEADER_FONT_SIZE, "coodoo.fileexport.xls.font.size");
                XSL_HEADER_FONT_BOLD = loadProperty(XSL_HEADER_FONT_BOLD, "coodoo.fileexport.xls.font.bold");
                XSL_HEADER_FONT_ITALIC = loadProperty(XSL_HEADER_FONT_ITALIC, "coodoo.fileexport.xls.font.italic");
                XSL_HEADER_FONT_COLOR = loadProperty(XSL_HEADER_FONT_COLOR, "coodoo.fileexport.xls.font.color");
                XSL_HEADER_STYLE_WRAPTEXT = loadProperty(XSL_HEADER_STYLE_WRAPTEXT, "coodoo.fileexport.xls.style.wraptext");
                XSL_HEADER_STYLE_COLOR = loadProperty(XSL_HEADER_STYLE_COLOR, "coodoo.fileexport.xls.style.color");
                XSL_HEADER_STYLE_ALIGNMENT = loadProperty(XSL_HEADER_STYLE_ALIGNMENT, "coodoo.fileexport.xls.style.alignment");
                XSL_HEADER_STYLE_VERTICALALIGNMENT = loadProperty(XSL_HEADER_STYLE_VERTICALALIGNMENT, "coodoo.fileexport.xls.style.verticalalignment");

                XSL_BUFFER_LIMIT = loadProperty(XSL_BUFFER_LIMIT, "coodoo.fileexport.xls.buffer.limit");
                TIMESTAMP_PATTERN = loadProperty(TIMESTAMP_PATTERN, "coodoo.fileexport.export.timestamp.pattern");

                CSV_SEPARATOR = loadProperty(CSV_SEPARATOR, "coodoo.fileexport.csv.separator");
                CSV_QUOTES = loadProperty(CSV_QUOTES, "coodoo.fileexport.csv.quotes");
            }
        } catch (IOException e) {
            log.info("Couldn't read {}!", fileexportPropertiesFilename, e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.warn("Couldn't close {}!", fileexportPropertiesFilename, e);
            }
        }
    }

    private static String loadProperty(String value, String key) {

        String property = properties.getProperty(key);
        if (property == null) {
            return value;
        }
        log.info("File Export Property {} loaded: {}", key, property);
        return property;
    }

    private static int loadProperty(int value, String key) {
        String property = properties.getProperty(key);
        if (property != null) {
            try {
                log.info("File Export Property {} loaded: {}", key, property);
                return Integer.valueOf(property).intValue();
            } catch (NumberFormatException e) {
                log.warn("File Export Property {} value invalid: {}", key, property);
            }
        }
        return value;
    }

    private static boolean loadProperty(boolean value, String key) {
        String property = properties.getProperty(key);
        if (property != null) {
            log.info("File Export Property {} loaded: {}", key, property);
            Boolean booleanValue = Boolean.valueOf(property);
            if (booleanValue != null) {
                return booleanValue;
            }
        }
        return value;
    }
}
