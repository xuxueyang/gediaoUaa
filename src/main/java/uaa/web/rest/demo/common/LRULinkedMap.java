package uaa.web.rest.demo.common;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRULinkedMap<K,V> {
//    LinkedHashMap 内部也有维护一个双向队列，在初始化时也会给定一个缓存大小的阈值。初始化时自定义是否需要删除最近不常使用的数据，如果是则会按照实现二中的方式管理数据。
//
//    其实主要代码就是重写了 LinkedHashMap 的 removeEldestEntry 方法:
//
//        1
//        2
//        3
//    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
//        return false;
//    }
//    它默认是返回 false，也就是不会管有没有超过阈值。
//
//    所以我们自定义大于了阈值时返回 true，这样 LinkedHashMap 就会帮我们删除最近最少使用的数据。
    /**
     * 最大缓存大小
     */
    private int cacheSize;
    private LinkedHashMap<K,V> cacheMap ;
    public LRULinkedMap(int cacheSize) {
        this.cacheSize = cacheSize;
        cacheMap = new LinkedHashMap(16,0.75F,true){
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                if (cacheSize + 1 == cacheMap.size()){
                    return true ;
                }else {
                    return false ;
                }
            }
        };
    }
    public void put(K key,V value){
        cacheMap.put(key,value) ;
    }
    public V get(K key){
        return cacheMap.get(key) ;
    }
    public Collection<Map.Entry<K, V>> getAll() {
        return new ArrayList<Map.Entry<K, V>>(cacheMap.entrySet());
    }

    @Test
    public void put() throws Exception {
        LRULinkedMap<String,Integer> map = new LRULinkedMap(3) ;
        map.put("1",1);
        map.put("2",2);
        map.put("3",3);
        for (Map.Entry<String, Integer> e : map.getAll()){
            System.out.print(e.getKey() + " : " + e.getValue() + "\t");
        }
        System.out.println("");
        map.put("4",4);
        for (Map.Entry<String, Integer> e : map.getAll()){
            System.out.print(e.getKey() + " : " + e.getValue() + "\t");
        }
    }

}
