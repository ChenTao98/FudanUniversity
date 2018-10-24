package artStore.servlet;

import artStore.entity.ArtistsEntity;
import artStore.entity.ArtworksEntity;
import artStore.service.GetArtistService;
import artStore.service.GetArtworkService;
import artStore.service.impl.GetArtistServiceImpl;
import artStore.service.impl.GetArtworkServiceImpl;

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
@WebServlet(name = "ArtistDetailServlet",value = "/ArtistDetailServlet")
public class ArtistDetailServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        获取作家ID
        String artostID=request.getParameter("id");
//        创建实例，获取该作家信息与作品
        GetArtworkService getArtworkService=new GetArtworkServiceImpl();
        List<ArtworksEntity> artworksEntityList=getArtworkService.getByArtistID(artostID);
        GetArtistService getArtistService=new GetArtistServiceImpl();
        List<ArtistsEntity> artistsEntityList=getArtistService.getOne(artostID);
//        设置request属性
        request.setAttribute("artwork",artworksEntityList);
        request.setAttribute("artist",artistsEntityList);
//        重定向到作家详细页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("./ArtistDetail.jsp");
        dispatcher.forward(request, response);
    }
}
