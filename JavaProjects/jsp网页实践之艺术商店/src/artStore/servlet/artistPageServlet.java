package artStore.servlet;

import artStore.entity.ArtistsEntity;
import artStore.service.GetArtistService;
import artStore.service.impl.GetArtistServiceImpl;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Bing Chen on 2017/7/23.
 */
@WebServlet(name = "artistPageServlet", value = "/artistPageServlet")
public class artistPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        设置编码
        response.setContentType("text/html;charset=utf-8");
//        获取所有作家
        GetArtistService getArtistService = new GetArtistServiceImpl();
        List<ArtistsEntity> artistsEntityList = getArtistService.getAll();
//        转化为Json格式，并返回
        JSONArray jsonArray = JSONArray.fromObject(artistsEntityList);
        System.out.println(jsonArray.toString());
        PrintWriter out = response.getWriter();
        out.print(jsonArray);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
