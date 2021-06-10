package com.imooc.mgallery.dao;

import com.imooc.mgallery.entity.Painting;
import com.imooc.mgallery.utils.PageModel;
import com.imooc.mgallery.utils.XmlDataSource;

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

    public Painting findById(Integer id){
        List<Painting> data = XmlDataSource.getRawData();
        Painting painting = null;
        for (Painting p : data) {
            if (p.getId() == id){
                painting = p;
                break;
            }
        }
        return painting;
    }
    public void update(Painting painting) {
        XmlDataSource.update(painting);
    }

    public void delete(Integer id) {
        XmlDataSource.delete(id);
    }
}
