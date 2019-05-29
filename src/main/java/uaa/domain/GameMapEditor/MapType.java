package uaa.domain.GameMapEditor;

import java.util.ArrayList;
import java.util.List;

public class MapType {
    public static class Type{
        public int id;
        public String name = "未命名";
        public Type(int id,String name){
            this.id = id;
            this.name = name;
        }
    }
    private static List<Type> allType = new ArrayList<>();
    static {
        int id = 1;
        allType.add(new Type(id++,"普通的地图"));
        allType.add(new Type(id++,"边界"));
        allType.add(new Type(id++,"障碍物"));
        allType.add(new Type(id++,"随机资源点"));
        allType.add(new Type(id++,"棋子出生点"));
    }

    public static List<Type> getAllMapType() {

        return allType;
    }
}
