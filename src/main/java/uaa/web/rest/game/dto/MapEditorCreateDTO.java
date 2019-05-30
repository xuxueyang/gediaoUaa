package uaa.web.rest.game.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

public class MapEditorCreateDTO {
    @ApiModelProperty(value = "名称，用来表示这是什么")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "图片")
    private String imagePath;

    @ApiModelProperty(value = "row")
    private String row;

    @ApiModelProperty(value = "col")
    private String col;


    @ApiModelProperty(value = "序列化特殊属性")
    private Map<String,String> object = new HashMap<>();

    @ApiModelProperty(value = "标识类型")
    private String mapTypeId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Map<String, String> getObject() {
        return object;
    }

    public void setObject(Map<String, String> object) {
        this.object = object;
    }

    public String getMapTypeId() {
        return mapTypeId;
    }

    public void setMapTypeId(String mapTypeId) {
        this.mapTypeId = mapTypeId;
    }
    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

}
