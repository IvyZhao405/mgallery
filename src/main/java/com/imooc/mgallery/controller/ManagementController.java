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
import java.io.File;
import java.io.IOException;
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
}
