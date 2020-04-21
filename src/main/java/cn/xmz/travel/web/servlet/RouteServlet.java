package cn.xmz.travel.web.servlet;

import cn.xmz.travel.domain.PageBean;
import cn.xmz.travel.domain.Route;
import cn.xmz.travel.domain.User;
import cn.xmz.travel.service.FavoriteService;
import cn.xmz.travel.service.RouteImgService;
import cn.xmz.travel.service.RouteService;
import cn.xmz.travel.service.impl.FavoriteServiceImpl;
import cn.xmz.travel.service.impl.RouteImgServiceImpl;
import cn.xmz.travel.service.impl.RouteServiceImpl;
import cn.xmz.travel.util.UuidUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    private RouteImgService routeImgService = new RouteImgServiceImpl();

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
        String ridStr = request.getParameter("rid");
        int rid = 0;
        if(ridStr != null &&ridStr.length() > 0 && !"null".equals(ridStr)){
            try{
                rid = Integer.parseInt(ridStr);
            }catch (Exception e){
                rid = 0;
            }
        }
//        if("null".equals(rname)||rname==null||rname=="") {
//            rname = "";
//        }else{
//            rname = new String(rname.getBytes("iso-8859-1"),"utf-8");
//        }


        int cid = 0;//类别id
        boolean crank = false;
        int beginPrice = 0;
        int endPrice = 0;
        //2.处理参数
        if(rank != null && rank.length() > 0 && "1".equals(rank)){
            crank = true;
        }
        if(bprice!=null && bprice.length()>0 && !"null".equals(bprice)){
            try{
                beginPrice = Integer.parseInt(bprice);
                if(eprice!=null && eprice.length()>0 && !"null".equals(eprice)){
                    endPrice = Integer.parseInt(eprice);
                }
            }catch (Exception e){
                beginPrice=0;
                endPrice =0;
            }

        }
        if(cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)){
            try {
                cid = Integer.parseInt(cidStr);
            }catch (Exception e){
                cid = 0;
            }
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
        PageBean<Route> pb = routeService.pageQuery(cid,currentPage, pageSize,rname,crank,beginPrice,endPrice,rid);

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

    /**
     * 修改个人信息功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        String m = request.getParameter("m");
        HttpSession session = request.getSession();

        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        //2.封装对象
        Route route = new Route();
        try {
            BeanUtils.populate(route,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service完成注册
        //UserService service = new UserServiceImpl();
        boolean re = routeService.update(route);

        //将info对象序列化为json
        writeValue(re, response);

    }


    /**
     * 添加路线
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //存放返回结果
        Route reRoute = null;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        List items = null;
        Map params = new HashMap();
        String pathM = request.getServletContext().getRealPath("/");
        String pathImg = "img\\product\\small\\";   //主图
        List<String> imgUrls= new ArrayList<String>();
        try{

            items = upload.parseRequest(request);
            int count = 0;
            Route r = new Route();
            for(Object object:items){
                FileItem fileItem = (FileItem) object;
                if (fileItem.isFormField()) { //文字数据
                    params.put(fileItem.getFieldName(), fileItem.getString("utf-8"));//如果你页面编码是utf-8的
                }else {
                    params.put(fileItem.getName(), fileItem.getString());
                    String imgName = "a"+ UuidUtil.getUuid();
                    if(count==0){
                        //把第一个写入route表里
                        r.setRimage(pathImg+imgName+".jpg");
                        count++;
                    }
                    fileItem.write(new File(pathM+pathImg+imgName+".jpg"));
                    imgUrls.add(pathImg+imgName+".jpg");
                }
            }


            r.setRname((String) params.get("rname"));
            r.setRouteIntroduce((String) params.get("routeIntroduce"));
            r.setDetail((String) params.get("routeIntroduce"));
            r.setRflag("1");

            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            r.setRdate(dateString);

            String priceS = (String) params.get("price");
            String sidS = (String) params.get("sid");
            String sourceIdS = (String) params.get("sourceId");
            String cidS = (String) params.get("cid");
            if(priceS!=null&&priceS!=""&&!"null".equals(priceS)){
                r.setPrice(Double.parseDouble(priceS));
            }
            if(sourceIdS!=null&&sourceIdS!=""&&!"null".equals(sourceIdS)){
                r.setSourceId(Integer.parseInt(sourceIdS));
            }
            if(sidS!=null&&sidS!=""&&!"null".equals(sidS)){
                r.setSid(Integer.parseInt(sidS));
            }
            if(cidS!=null&&cidS!=""&&!"null".equals(cidS)){
                r.setCid(Integer.parseInt(cidS));
            }

            reRoute = routeService.add(r);
            if(reRoute!=null&&reRoute.getRid()>0) {
                for (int i = 0; i < imgUrls.size(); i++) {
                    String name = imgUrls.get(i);
                    routeImgService.add(reRoute.getRid(), name, name);
                }
            }

        }catch (FileUploadException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //将info对象序列化为json
        writeValue(reRoute, response);

    }

}

