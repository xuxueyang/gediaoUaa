package uaa.web.rest.util.excel.vo.app.log;

import uaa.domain.base.JModel;
import uaa.web.rest.util.excel.basel.ColumnWalker;
import uaa.web.rest.util.excel.basel.ColumnWalkerImpl;
import uaa.web.rest.util.excel.basel.ExcelColumn;
import uaa.web.rest.util.excel.basel.ExcelHead;

import java.time.ZonedDateTime;

@ExcelHead("格調便簽導出")
public class LogEachOutVO extends ColumnWalkerImpl implements JModel,ColumnWalker {
    @ExcelColumn("创建时间")
    private ZonedDateTime createdDate;
    @ExcelColumn("更新时间")
    private ZonedDateTime updatedDate;
    @ExcelColumn("所属日期")
    private String belongDate;
    @ExcelColumn("类型")
    private String type;

    @ExcelColumn("标题")
    private String title;

    @ExcelColumn("简略")
    private String message;

    @ExcelColumn("标签")
    private String tags;
    @ExcelColumn("详细信息")
    private String detail;

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(ZonedDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getBelongDate() {
        return belongDate;
    }

    public void setBelongDate(String belongDate) {
        this.belongDate = belongDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


}
