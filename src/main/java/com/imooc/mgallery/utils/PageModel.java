package com.imooc.mgallery.utils;

import java.util.ArrayList;
import java.util.List;

//Page Object
public class PageModel {
    private int page; //current page number
    private int totalPages; //total # of pages
    private int rows; //rows in each page
    private int totalRows; //total rows in the website
    private int pageStartRow; //current page's start row #
    private int pageEndRow; //current page's end row #
    private boolean hasNextPage; //current page has next page
    private boolean hasPreviousPage; //current page has previous page
    private List pageData; //data in current page

    public PageModel(){

    }

    /**
     * PageModel constructor, initialize member fields
     * @param data original data collection
     * @param page current page number
     * @param rows rows in each page
     */
    public PageModel(List data, int page, int rows) {
        this.page = page;
        this.rows = rows;
        totalRows = data.size();
        //total pages rule: totalRows/rows, get the next integer.
        //Math.floor get smaller integer.
        totalPages = new Double(Math.ceil(totalRows/(rows * 1f))).intValue();

        pageStartRow = (page - 1) * rows;
        pageEndRow = page * rows;
        //totalRows: 20 | totalPage: 4 | rows:6
        //pageEndRow=4*6=24 > 20， subList() throws outofbound exception
        if(pageEndRow > totalRows){
            pageEndRow = totalRows;
        }
        pageData = data.subList(pageStartRow, pageEndRow); // get current page's data
        if (page > 1){
            hasPreviousPage = true;
        }else {
            hasPreviousPage = false;
        }
        if (page < totalPages) {
            hasNextPage = true;
        } else {
            hasNextPage = false;
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getPageStartRow() {
        return pageStartRow;
    }

    public void setPageStartRow(int pageStartRow) {
        this.pageStartRow = pageStartRow;
    }

    public int getPageEndRow() {
        return pageEndRow;
    }

    public void setPageEndRow(int pageEndRow) {
        this.pageEndRow = pageEndRow;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public List getPageData() {
        return pageData;
    }

    public void setPageData(List pageData) {
        this.pageData = pageData;
    }

    public static void main(String[] args) {
        List sample = new ArrayList();
        for(int i = 1; i <= 100; i++) {
            sample.add(i);
        }
        PageModel pageModel = new PageModel(sample, 6, 8);
        System.out.println(pageModel.getPageData());
        System.out.println(pageModel.getTotalPages());
        System.out.println(pageModel.getPageStartRow() + ":" + pageModel.getPageEndRow());
    }
}
