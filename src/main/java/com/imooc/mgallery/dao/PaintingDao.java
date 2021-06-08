package com.imooc.mgallery.dao;

import com.imooc.mgallery.entity.Painting;
import com.imooc.mgallery.utils.PageModel;
import com.imooc.mgallery.utils.XmlDataSource;
import sun.jvm.hotspot.debugger.Page;

import java.util.ArrayList;
import java.util.List;

public class PaintingDao {
    /**
     * get page data for a specified page
     * @param page page number
     * @param rows rows in every page
     * @return PageModel page object
     */
    public PageModel pagination(int page, int rows){
        List<Painting> list = XmlDataSource.getRawData();
        PageModel pageModel = new PageModel(list, page, rows);
        return pageModel;
    }

    public PageModel pagination(int category, int page, int rows){
        List<Painting> list = XmlDataSource.getRawData();
        List<Painting> categoryList= new ArrayList<>();
        for(Painting p:list){
            if(p.getCategory() == category){
                categoryList.add(p);
            }
        }
        PageModel pageModel = new PageModel(categoryList, page, rows);
        return pageModel;
    }

    /**
     * add new painting
     * @param painting
     */
    public void create(Painting painting){
        XmlDataSource.append(painting);
    }
}
