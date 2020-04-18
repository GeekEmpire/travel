package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {

    private CategoryService service = new CategoryServiceImpl();

    /**
     * 查询所有
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.调用service查询所有
        String cid = request.getParameter("cid");
        String cname = request.getParameter("cname");
//        boolean re = false;
        List<Category> cs = null;
        if(cid != null&&cid!=""&&!"null".equals(cid)) {
            int id =0;
            id = Integer.parseInt(cid);
            cs = service.findBySearch(id, cname);
        }else if(cname != null&&cname!=""&&!"null".equals(cname)){
            cs = service.findBySearch(0, cname);
        } else {
            cs = service.findAll();
        }
        //2.序列化json返回
        writeValue(cs,response);

    }

    /**
     * 添加
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> map = request.getParameterMap();
        //2.封装对象
        Category c = new Category();
        try {
            BeanUtils.populate(c,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        boolean re = service.add(c);
        writeValue(re,response);
    }

    /**
     * 修改
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String cid = request.getParameter("cid");
        String cname = request.getParameter("cname");
        boolean re = false;
        int id =0;
        if(cid != null&&cid!=""&&!"null".equals(cid)&&cname!= null&&cname!=""&&!"null".equals(cname)) {
            id = Integer.parseInt(cid);
            re = service.update(cname, id);
        }
        writeValue(re,response);

    }


    /**
     * 删除
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        boolean re = false;
        int id =0;
        if(cid != null&&cid!=""&&!"null".equals(cid)) {
            id = Integer.parseInt(cid);
            re = service.remove(id);
        }
        writeValue(re,response);
    }

}
