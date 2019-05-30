package uaa.domain.GameMapEditor;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uaa.domain.GameMapEditor.AbstractClass.AbstractMapEachBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "game_map_editor_map_editor_base")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MapEditorBase extends AbstractMapEachBase {
    //标记自己是属于那张地图的
    @Column(name = "MAP_EDITOR_ID")
    private String mapEditorId;

    @Column(name = "ROW")
    private String row;

    @Column(name = "COL")
    private String col;


    // 地图资源点
    @Column(name = "STATUS")
    private String status;


    @Column(name = "CREATE_ID")
    private String createId;

    @Column(name = "UPDATE_ID")
    private String updateId;

    @ApiModelProperty(value = "ID")
    @Column(name = "ID")
    @Id
    private String id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getMapEditorId() {
        return mapEditorId;
    }

    public void setMapEditorId(String mapEditorId) {
        this.mapEditorId = mapEditorId;
    }
}
