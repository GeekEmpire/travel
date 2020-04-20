package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.SellerService;
import cn.itcast.travel.service.impl.SellerServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/seller/*") // /user/add /user/find
public class SellerServlet extends BaseServlet {

    //声明UserService业务对象
    private SellerService service = new SellerServiceImpl();

    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //验证校验
        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        //2.封装对象
        Seller seller = new Seller();
        try {
            BeanUtils.populate(seller,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service完成注册
        int status = service.regist(seller);
        ResultInfo info = new ResultInfo();
        //4.响应结果
        if(status==200){
            //注册成功
            info.setFlag(true);
        }else if(status == 201){
            //用户名已存在
            info.setFlag(false);
            info.setErrorMsg("商家已存在!");
        }else{
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败!");
        }

        //将info对象序列化为json
        writeValue(info, response);

    }

    /**
     * 登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    /*
     * 查询单个对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Seller seller = null;
        String sidStr = request.getParameter("sid");
        int sid = 0;
        if(sidStr != null&&sidStr!=""&&!"null".equals(sidStr)){
            sid = Integer.parseInt(sidStr);
        }
        seller = service.findOne(sid);
        writeValue(seller,response);
    }



    /**
     * 修改商家信息功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        //2.封装对象
        Seller seller = new Seller();
        try {
            BeanUtils.populate(seller,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service完成注册
        int status = service.update(seller);
        ResultInfo info = new ResultInfo();
        //4.响应结果
        if(status==200){
            //更新成功
            info.setFlag(true);
        }else{
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("修改失败!");
        }
        //将info对象序列化为json
        writeValue(info, response);

    }


    /**
     * 注销（禁用）账号
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码
        String sid = request.getParameter("sid");
        if(sid != null&&sid!=""&&!"null".equals(sid)){
            //2.调用service完成激活
            //UserService service = new UserServiceImpl();
            int id = Integer.parseInt(sid);
            boolean flag = service.remove(id);
            writeValue(flag, response);
        }
    }


    /**
     * 查询所有
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");

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
        }else{
            pageSize = 10;   //list 10个一页
        }


        //3. 调用service查询PageBean对象
        PageBean<Seller> pb = service.pageQuery(currentPage, pageSize);

        //4. 将pageBean对象序列化为json，返回
        writeValue(pb,response);

    }

}
