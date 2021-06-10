package com.imooc.mgallery.service;

import com.imooc.mgallery.dao.PaintingDao;
import com.imooc.mgallery.entity.Painting;
import com.imooc.mgallery.utils.PageModel;

import java.util.List;

public class PaintingService {
    private PaintingDao paintingDao = new PaintingDao();

    /**
     *
     * @param page current page
     * @param rows rows in each page
     * @param category painting category
     * @return PageModel
     */
    public PageModel pagination(int page, int rows, String...category){
        if (rows == 0) {
            throw new RuntimeException("Invalid rows input parameter");
        }
        if (category.length == 0 || category[0] == null){
            return paintingDao.pagination(page, rows);
        } else{
            return paintingDao.pagination(Integer.parseInt(category[0]), page, rows);
        }
    }

    /**
     * add new painting
     * @param painting new painting
     */
    public void create(Painting painting){
        paintingDao.create(painting);
    }

    public Painting findById(Integer id){
        Painting p = paintingDao.findById(id);
        if (p==null) {
            throw new RuntimeException("[id=" + id +"] painting doesn't exist");
        }
        return p;
    }

    public void update(Painting newPainting,Integer isPreviewModified) {
        //Update original data
        Painting oldPainting = this.findById(newPainting.getId());
        oldPainting.setPname(newPainting.getPname());
        oldPainting.setCategory(newPainting.getCategory());
        oldPainting.setPrice(newPainting.getPrice());
        oldPainting.setDescription(newPainting.getDescription());
        if(isPreviewModified == 1) {
            oldPainting.setPreview(newPainting.getPreview());
        }
        paintingDao.update(oldPainting);
    }

    public void delete(Integer id) {
        paintingDao.delete(id);
    }

    public static void main(String[] args) {
        PaintingService paintingService = new PaintingService();
        PageModel pageModel = paintingService.pagination(2, 6);
        List<Painting> paintingList = pageModel.getPageData();
        for (Painting painting: paintingList) {
            System.out.println(painting.getPname());
        }
    }
}
