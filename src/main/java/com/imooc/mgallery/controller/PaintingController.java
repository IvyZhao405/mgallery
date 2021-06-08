package com.imooc.mgallery.controller;

import com.imooc.mgallery.service.PaintingService;
import com.imooc.mgallery.utils.PageModel;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "PaintingController", value = "/page")
public class PaintingController extends HttpServlet {
    private final PaintingService paintingService = new PaintingService();

    /**
     * @See HttpServlet#doGet()
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. received HTTP request data
        String page = request.getParameter("p");
        String rows = request.getParameter("r");
        String category = request.getParameter("c");
        if (page == null) {
            page = "1";
        }
        if (rows == null) {
            rows = "6";
        }
        //2. Use Service to process request for result
        PageModel pageModel = paintingService.pagination(Integer.parseInt(page), Integer.parseInt(rows), category);
        // javaBean
        request.setAttribute("pageModel", pageModel);
        //3. Send request to corresponding JSP(VIEW)for display
        request.getRequestDispatcher("WEB-INF/jsp/index.jsp").forward(request, response);
    }

}
