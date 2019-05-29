package uaa.domain.GameMapEditor.AbstractClass;


import io.swagger.annotations.ApiModelProperty;
import uaa.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 每一个地图片应该具有的属性
 */
public  abstract class AbstractMapEachBase extends BaseEntity implements Serializable{

    @ApiModelProperty(value = "名称，用来表示这是什么")
    @Column(name = "TITLE")
    private String title;

    @ApiModelProperty(value = "描述")
    @Column(name = "DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "图片")
    @Column(name = "IMAGE_PATH")
    private String imagePath;


    @ApiModelProperty(value = "序列化特殊属性")
    @Column(name = "OBJECT")
    private String object;

    @ApiModelProperty(value = "标识类型")
    @Column(name = "MAP_TYPE_ID")
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



    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }




    public String getMapTypeId() {
        return mapTypeId;
    }

    public void setMapTypeId(String mapTypeId) {
        this.mapTypeId = mapTypeId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
