package uaa.web.rest.util.excel.basel;

import java.util.ArrayList;
import java.util.List;

public class ExcelModel {
    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<? extends ColumnWalker> getRecordColumns() {
        return recordColumns;
    }

    public void setRecordColumns(List<? extends ColumnWalker> recordColumns) {
        this.recordColumns = recordColumns;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    private String sheetName = "sheet1";
    private String title = "";
    private List<String> columns = new ArrayList();
    private List<? extends ColumnWalker> recordColumns = new ArrayList();
    private String detail;

    public ExcelModel() {
    }


}
