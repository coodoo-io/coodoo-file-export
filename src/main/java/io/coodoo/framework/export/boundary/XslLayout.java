package io.coodoo.framework.export.boundary;

import io.coodoo.framework.export.control.FileExportConfig;

public class XslLayout {

    private XslCellLayout title;

    private XslCellLayout header;

    private XslCellLayout row;

    public XslCellLayout getTitle() {
        if (title == null) {
            return getHeader();
        }
        return title;
    }

    public void setTitle(XslCellLayout title) {
        this.title = title;
    }

    public XslCellLayout getHeader() {
        if (header == null) {
            return new XslCellLayout(FileExportConfig.XSL_HEADER_STYLE_WRAPTEXT, FileExportConfig.XSL_HEADER_STYLE_COLOR,
                            FileExportConfig.XSL_HEADER_STYLE_ALIGNMENT, FileExportConfig.XSL_HEADER_STYLE_VERTICALALIGNMENT,
                            FileExportConfig.XSL_HEADER_HEIGTH, FileExportConfig.XSL_HEADER_FONT_NAME, FileExportConfig.XSL_HEADER_FONT_SIZE,
                            FileExportConfig.XSL_HEADER_FONT_BOLD, FileExportConfig.XSL_HEADER_FONT_ITALIC, FileExportConfig.XSL_HEADER_FONT_COLOR);
        }
        return header;
    }

    public void setHeader(XslCellLayout header) {
        this.header = header;
    }

    public XslCellLayout getRow() {
        if (row == null) {
            return new XslCellLayout(FileExportConfig.XSL_STYLE_WRAPTEXT, FileExportConfig.XSL_STYLE_COLOR, FileExportConfig.XSL_STYLE_ALIGNMENT,
                            FileExportConfig.XSL_STYLE_VERTICALALIGNMENT, FileExportConfig.XSL_HEIGTH, FileExportConfig.XSL_FONT_NAME,
                            FileExportConfig.XSL_FONT_SIZE, FileExportConfig.XSL_FONT_BOLD, FileExportConfig.XSL_FONT_ITALIC, FileExportConfig.XSL_FONT_COLOR);
        }
        return row;
    }

    public void setRow(XslCellLayout row) {
        this.row = row;
    }

}
