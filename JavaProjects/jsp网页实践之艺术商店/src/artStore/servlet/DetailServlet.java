package artStore.servlet;

import artStore.entity.ArtworksEntity;
import artStore.entity.GenresEntity;
import artStore.entity.SubjectsEntity;
import artStore.service.GetArtworkService;
import artStore.service.impl.GetArtworkServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by Bing Chen on 2017/7/18.
 */
@WebServlet(name = "DetailServlet",value = "/DetailServlet")
public class DetailServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        获取商品ID
        String artworkId=request.getParameter("id");
//        获取商品的所有信息
        GetArtworkService getArtworkService=new GetArtworkServiceImpl();
        getArtworkService.amountAdd(parseInt(artworkId));
        List<ArtworksEntity> artworksEntityList=getArtworkService.getOne(artworkId);
        List<SubjectsEntity> subjectsEntityList=getArtworkService.getSubject(artworkId);
        List<GenresEntity> genresEntityList=getArtworkService.getGenres(artworkId);
        request.setAttribute("imageDetail",artworksEntityList);
        request.setAttribute("imageSubject",subjectsEntityList);
        request.setAttribute("imageGenres",genresEntityList);
//        重定向到商品详细页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("./Detail.jsp");
        dispatcher.forward(request, response);
    }
}
