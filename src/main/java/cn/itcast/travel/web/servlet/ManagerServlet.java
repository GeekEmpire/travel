package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Manager;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.ManagerService;
import cn.itcast.travel.service.impl.ManagerServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/manager/*")
public class ManagerServlet extends BaseServlet {

    ManagerService managerService = new ManagerServiceImpl();

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
        Manager manager = new Manager();
        try {
            BeanUtils.populate(manager,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用Service查询
        Manager m  = managerService.login(manager);

        ResultInfo info = new ResultInfo();

        //4.判断用户对象是否为null
        if(m == null){
            //用户名密码或错误
            info.setFlag(false);
            info.setErrorMsg("用户名密码或错误");
        }
        if(m != null){
            request.getSession().setAttribute("manager",m);//登录成功标记
            //登录成功
            info.setFlag(true);
        }
        //响应数据
        writeValue(info, response);
    }

}
