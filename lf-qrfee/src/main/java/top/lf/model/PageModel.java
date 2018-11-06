package top.lf.model;

import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: YuanYan
 * @Create At: 2018-11-06 10:20
 * @Description: TableView 分页类
 */
public class PageModel<T> implements Serializable {
    private static final long serialVersionUID = -61380926948440031L;
    private SimpleIntegerProperty totalRecord;//总记录数
    private SimpleIntegerProperty pageSize;//每页显示数
    private SimpleIntegerProperty totalPage;//总页数
    private SimpleIntegerProperty pageInx;//当前页码
    private List<T> dataList;//本页数据

    public SimpleIntegerProperty totalRecordProperty() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord.set(totalRecord);
    }

    public int getPageSize() {
        return pageSize.get();
    }

    public SimpleIntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize.set(pageSize);
    }

    public int getTotalPage() {
        return totalPage.get();
    }

    public SimpleIntegerProperty totalPageProperty() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage.set(totalPage);
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getPageInx() {
        return pageInx.get();
    }

    public SimpleIntegerProperty pageInxProperty() {
        return pageInx;
    }

    public void setPageInx(int pageInx) {
        this.pageInx.set(pageInx);
    }

    public PageModel(SimpleIntegerProperty totalRecord, SimpleIntegerProperty pageSize, List<T> dataList) {
        this.totalRecord = totalRecord;
        this.pageSize = pageSize;
        this.dataList = dataList;
    }
}
