package com.imooc.mgallery.controller;

import com.imooc.mgallery.entity.Painting;
import com.imooc.mgallery.service.PaintingService;
import com.imooc.mgallery.utils.PageModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

/**
 * Backend management controller
 */
@WebServlet(name = "ManagementController", value = "/management")
public class ManagementController extends HttpServlet {
    private PaintingService paintingService = new PaintingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        String method = request.getParameter("method");
        if (method.equals("list")){
            this.list(request, response);
        }else if(method.equals("show_create")) {
            this.showCreatePage(request, response);
        }else if (method.equals("create")){
            this.create(request, response);
        }else if (method.equals("show_update")){
            this.showUpdatePage(request, response);
        }else if (method.equals("update")){
            System.out.println("in update");
            this.update(request, response);
        }else if (method.equals("delete")){
            this.delete(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = request.getParameter("p");
        String rows = request.getParameter("r");
        if (page == null) {
            page = "1";
        }
        if (rows == null) {
            rows = "6";
        }
        PageModel pageModel = paintingService.pagination(Integer.parseInt(page), Integer.parseInt(rows));
        request.setAttribute("pageModel", pageModel);
        request.getRequestDispatcher("WEB-INF/jsp/list.jsp").forward(request, response);
    }

    private void showCreatePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/jsp/create.jsp").forward(request, response);
    }

    private void create(HttpServletRequest request, HttpServletResponse response) {
        String pname = request.getParameter("pname");
        System.out.println(pname);
        //1. Initialize FileUpload
        FileItemFactory factory = new DiskFileItemFactory();
        /**
         * FileItemFactory: Convert front-end form data to FileItem object.
         * ServletFileUpload: Interpretation HTTP request for Java Web's FileUpload.
         */
        ServletFileUpload sf = new ServletFileUpload(factory);
        //2. Iterate all FileItem
        try {
            List<FileItem> formData = sf.parseRequest(request);
            Painting painting = new Painting();
            for (FileItem fi: formData) {
                if (fi.isFormField()==true){
                    System.out.println("普通输入项:" + fi.getFieldName());
                    switch(fi.getFieldName()){
                        case "pname":
                            painting.setPname(fi.getString("UTF-8"));
                            break;
                        case "category":
                            painting.setCategory(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "price":
                            painting.setPrice(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "description":
                            painting.setDescription(fi.getString("UTF-8"));
                            break;
                        default:
                            break;
                    }
                }else {
                    System.out.println("文件上传项:" + fi.getFieldName());
                    //3. Save file to a directory
                    String path = request.getServletContext().getRealPath("upload");
                    System.out.println("文件上传目录:" + path);
//                    String fileName = "test.jpg";
                    String fileName = UUID.randomUUID().toString();
                    //fi.getName()get orinial file name, get file extension string after ".".
                    String suffix = fi.getName().substring(fi.getName().lastIndexOf("."));
                    //fi.wirte()write to file
                    fi.write(new File(path, fileName + suffix));
                    painting.setPreview("upload/" + fileName + suffix);
                }
            }
            paintingService.create(painting); //new painting is added.
            response.sendRedirect("management?method=list"); // return to painting list
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showUpdatePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Painting painting = paintingService.findById(Integer.parseInt(id));
        request.setAttribute("painting", painting);
        request.getRequestDispatcher("WEB-INF/jsp/update.jsp").forward(request, response);
    }

    private void update(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        int isPreviewModified = 0;
        //文件上传时的数据处理与标准表单完全不同
		/*
		String pname = request.getParameter("pname");
		System.out.println(pname);
		*/
        //1. 初始化FileUpload组件
        FileItemFactory factory = new DiskFileItemFactory();
        /**
         * FileItemFactory 用于将前端表单的数据转换为一个个FileItem对象
         * ServletFileUpload 则是为FileUpload组件提供Java web的Http请求解析
         */
        ServletFileUpload sf = new ServletFileUpload(factory);
        //2. 遍历所有FileItem
        try {
            List<FileItem> formData = sf.parseRequest(request);
            Painting painting = new Painting();
            for(FileItem fi:formData) {
                if(fi.isFormField()) {
                    System.out.println("普通输入项:" + fi.getFieldName() + ":" + fi.getString("UTF-8"));
                    switch (fi.getFieldName()) {
                        case "pname":
                            painting.setPname(fi.getString("UTF-8"));
                            break;
                        case "category":
                            painting.setCategory(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "price":
                            painting.setPrice(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "description":
                            painting.setDescription(fi.getString("UTF-8"));
                            break;
                        case "id":
                            painting.setId(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "isPreviewModified":
                            isPreviewModified = Integer.parseInt(fi.getString("UTF-8"));
                            break;
                        default:
                            break;
                    }
                }else {
                    if(isPreviewModified == 1) {
                        System.out.println("文件上传项:" + fi.getFieldName());
                        //3.文件保存到服务器目录
                        String path = request.getServletContext().getRealPath("upload");
                        System.out.println("上传文件目录:" + path);
                        //String fileName = "test.jpg";
                        String fileName = UUID.randomUUID().toString();
                        //fi.getName()得到原始文件名,截取最后一个.后所有字符串,例如:wxml.jpg->.jpg
                        String suffix = fi.getName().substring(fi.getName().lastIndexOf("."));
                        //fi.write()写入目标文件
                        fi.write(new File(path,fileName + suffix));
                        painting.setPreview("upload/" + fileName + suffix);
                    }
                }
            }
            //更新数据的核心方法
            paintingService.update(painting, isPreviewModified);
            response.sendRedirect("management?method=list");//返回列表页
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        PrintWriter out = response.getWriter();
        try {
            paintingService.delete(Integer.parseInt(id));
            //{"result":"ok"}
            out.println("{\"result\":\"ok\"}");
        }catch(Exception e) {
            e.printStackTrace();
            out.println("{\"result\":\"" + e.getMessage() + "\"}");
        }

    }

}
