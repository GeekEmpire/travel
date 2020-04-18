package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.alibaba.druid.sql.visitor.functions.Char;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*") // /user/add /user/find
public class UserServlet extends BaseServlet {

    //声明UserService业务对象
    private UserService service = new UserServiceImpl();

    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //验证校验
        String m = request.getParameter("m");
        if(!"1".equals(m)){
            String check = request.getParameter("check");
            //从sesion中获取验证码
            HttpSession session = request.getSession();
            String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
            session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次
            //比较
            if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
                //验证码错误
                ResultInfo info = new ResultInfo();
                //注册失败
                info.setFlag(false);
                info.setErrorMsg("验证码错误");
                //将info对象序列化为json
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(info);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(json);
                return;
            }
        }
        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service完成注册
        //UserService service = new UserServiceImpl();
        int status = service.regist(user);
        ResultInfo info = new ResultInfo();
        //4.响应结果
        if(status==200){
            //注册成功
            info.setFlag(true);
        }else if(status == 201){
            //用户名已存在
            info.setFlag(false);
            info.setErrorMsg("用户名已存在!");
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
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用Service查询
       // UserService service = new UserServiceImpl();
        User u  = service.login(user);

        ResultInfo info = new ResultInfo();

        //4.判断用户对象是否为null
        if(u == null){
            //用户名密码或错误
            info.setFlag(false);
            info.setErrorMsg("用户名密码或错误");
        }
        //5.判断用户是否激活
        if(u != null && "N".equals(u.getStatus())){
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活");
        }
        if(u != null && "F".equals(u.getStatus())){
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您的账号已注销");
        }
        //6.判断登录成功
        if(u != null && "Y".equals(u.getStatus())){
            request.getSession().setAttribute("user",u);//登录成功标记

            //登录成功
            info.setFlag(true);
        }

        //响应数据
        writeValue(info, response);
    }

    /**
     * 查询单个对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        //将user写回客户端

       /* ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),user);*/
       writeValue(user,response);
    }

    /**
     * 退出功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.销毁session
        request.getSession().invalidate();

        //2.跳转登录页面
        response.sendRedirect(request.getContextPath()+"/login.html");
    }


    /**
     * 激活功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码
        String code = request.getParameter("code");
        if(code != null){
            //2.调用service完成激活
            //UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            //3.判断标记
            String msg = null;
            if(flag){
                //激活成功
                msg = "激活成功，请<a href='/travel/login.html'>登录</a>";
            }else{
                //激活失败
                msg = "激活失败，请联系管理员!";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }


    /**
     * 修改个人信息功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String m = request.getParameter("m");
        HttpSession session = request.getSession();
        if(!"1".equals(m)){  //如果不是管理员添加，验证
            //验证校验
            String check = request.getParameter("check");
            //从sesion中获取验证码
            String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
            session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次
            //比较
            if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
                //验证码错误
                ResultInfo info = new ResultInfo();
                //注册失败
                info.setFlag(false);
                info.setErrorMsg("验证码错误");
                //将info对象序列化为json
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(info);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(json);
                return;
            }
        }

        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service完成注册
        //UserService service = new UserServiceImpl();
        int status = service.update(user);
        ResultInfo info = new ResultInfo();
        //4.响应结果
        if(status==200){
            //更新成功
            info.setFlag(true);
            session.setAttribute("user", user);
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
    public void changeUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码
        String uid = request.getParameter("uid");
        if(uid != null&&uid!=""&&!"null".equals(uid)){
            //2.调用service完成激活
            //UserService service = new UserServiceImpl();
            int id = Integer.parseInt(uid);
            boolean flag = service.changeUser(id);
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

        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String sex = request.getParameter("sex");
        String telephone = request.getParameter("telephone");
        String email = request.getParameter("email");

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
        PageBean<User> pb = service.pageQuery(currentPage, pageSize,username,name,sex,telephone,email);

        //4. 将pageBean对象序列化为json，返回
        writeValue(pb,response);

    }

}
