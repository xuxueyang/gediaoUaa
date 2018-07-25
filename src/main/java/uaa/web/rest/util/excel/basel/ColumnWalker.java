package uaa.web.rest.util.excel.basel;


public interface ColumnWalker {
    /**
     * 下一个属性的值
     * next:
     *
     * @return
     * @author   xiongping
     */
    String next();

    /**
     * 是否有下一个值
     * hasNext:
     *
     * @return
     * @author   xiongping
     */
    boolean hasNext();
}
