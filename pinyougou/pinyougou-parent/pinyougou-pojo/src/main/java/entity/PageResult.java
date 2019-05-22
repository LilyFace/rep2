package entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: YangRunTao
 * @Description: 通用分页结果包装类
 * @Date: 2019/05/08 15:54
 * @Modified By:
 */
public class PageResult <T> implements Serializable {

    private static final long serialVersionUID = -3428403759645297852L;
    private long total;//总记录数
    private List<T> rows;//当前页结果

    public PageResult() {
    }

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
