package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        String rank = request.getParameter("rank");
        String bprice = request.getParameter("bprice");
        String eprice = request.getParameter("eprice");

        //接受rname 线路名称
        String rname = request.getParameter("rname");
        if("null".equals(rname)||rname==null||rname=="") {
            rname = "";
        }else{
            rname = new String(rname.getBytes("iso-8859-1"),"utf-8");
        }


        int cid = 0;//类别id
        boolean crank = false;
        int beginPrice = 0;
        int endPrice = 0;
        //2.处理参数
        if(rank != null && rank.length() > 0 && "1".equals(rank)){
            crank = true;
        }
        if(bprice!=null && bprice.length()>0 && !"null".equals(bprice)){
            beginPrice = Integer.parseInt(bprice);
            if(eprice!=null && eprice.length()>0 && !"null".equals(eprice)){
                endPrice = Integer.parseInt(eprice);
            }
        }
        if(cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;//当前页码，如果不传递，则默认为第一页
        if(currentPageStr != null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
            if(currentPage<=0) currentPage = 1;
        }else{
            currentPage = 1;
        }

        int pageSize = 0;//每页显示条数，如果不传递，默认每页显示5条记录
        if(pageSizeStr != null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else if(crank){
            pageSize = 8;  //排行榜八个一页
        }else {
            pageSize = 5;   //list五个一页
        }


        //3. 调用service查询PageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize,rname,crank,beginPrice,endPrice);

        //4. 将pageBean对象序列化为json，返回
        writeValue(pb,response);

    }

    /**
     * 根据id查询一个旅游线路的详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接收id
        String rid = request.getParameter("rid");
        //2.调用service查询route对象
        Route route = routeService.findOne(rid);
        //3.转为json写回客户端
        writeValue(route,response);
    }

    /**
     * 判断当前登录用户是否收藏过该线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取线路id
        String rid = request.getParameter("rid");

        //2. 获取当前登录的用户 user
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if(user == null){
            //用户尚未登录
            uid = 0;
        }else{
            //用户已经登录
            uid = user.getUid();
        }

        //3. 调用FavoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);

        //4. 写回客户端
        writeValue(flag,response);
    }

    /**
     * 添加收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取线路rid
        String rid = request.getParameter("rid");
        //2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if(user == null){
            //用户尚未登录
            writeValue(false,response);
            return ;
        }else{
            //用户已经登录
            uid = user.getUid();
        }


        //3. 调用service添加
        favoriteService.add(rid,uid);
        writeValue(true,response);
    }

    /**
     * 取消收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void removeFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取线路rid
        String rid = request.getParameter("rid");
        //2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if(user == null){
            //用户尚未登录
            writeValue(false,response);
            return ;
        }else{
            //用户已经登录
            uid = user.getUid();
        }


        //3. 调用service删除
        favoriteService.remove(rid,uid);
        writeValue(true,response);
    }

    /**
     * 查找用户收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findUserFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if(user == null){
            //用户尚未登录
            writeValue(false,response);
            return ;
        }else{
            //用户已经登录
            uid = user.getUid();
        }

        //3. 调用service查询
        List<Route> routes = new ArrayList<Route>();
        routes = routeService.findUserFavorite(uid);

        writeValue(routes,response);
    }

    /**
     * 查找用户分享
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findUserShared(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if(user == null){
            //用户尚未登录
            writeValue(false,response);
            return ;
        }else{
            //用户已经登录
            uid = user.getUid();
        }

        //3. 调用service查询
        List<Route> routes = new ArrayList<Route>();
        routes = routeService.findUserShared(uid);

        writeValue(routes,response);
    }

    /**
     * 移除路线、路线图片
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void removeRoute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取线路rid
        String rid = request.getParameter("rid");

        //3. 调用service删除
        boolean flag = routeService.removeRoute(Integer.parseInt(rid));
        writeValue(flag, response);
    }


}

