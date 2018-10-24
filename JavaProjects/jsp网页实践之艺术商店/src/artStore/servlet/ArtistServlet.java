package artStore.servlet;

import artStore.entity.ArtistsEntity;
import artStore.service.GetArtistService;
import artStore.service.impl.GetArtistServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Bing Chen on 2017/7/19.
 */
@WebServlet(name = "ArtistServlet",value="/ArtistServlet")
public class ArtistServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        获取所有作家
        GetArtistService getArtistService=new GetArtistServiceImpl();
        List<ArtistsEntity> artistsEntityList=getArtistService.getAll();
//        设置request属性
        request.setAttribute("artistAll",artistsEntityList);
//        重定向到作家页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("./Artist.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
