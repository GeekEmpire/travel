package cn.xmz.travel.service.impl;

import cn.xmz.travel.dao.*;
import cn.xmz.travel.dao.impl.*;
import cn.xmz.travel.domain.*;
import cn.xmz.travel.dao.*;
import cn.xmz.travel.dao.impl.*;
import cn.xmz.travel.domain.*;
import cn.xmz.travel.service.RouteService;
import cn.xmz.travel.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();

    private RouteImgDao routeImgDao = new RouteImgDaoImpl();

    private SellerDao sellerDao = new SellerDaoImpl();

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    private UserDao userDao = new UserDaoImpl();


    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname, boolean crank, int beginPrice, int endPrice, int rid) {
        //封装PageBean
        PageBean<Route> pb = new PageBean<Route>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);

        //设置总记录数
        int totalCount = routeDao.findTotalCount(cid, rname, beginPrice, endPrice, rid);
        pb.setTotalCount(totalCount);
        //设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;//开始的记录数
        List<Route> list = routeDao.findByPage(cid, start, pageSize, rname, crank, beginPrice, endPrice,rid);
        pb.setList(list);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);


        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //1.根据id去route表中查询route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));

        //2.根据route的id 查询图片集合信息
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        //2.2将集合设置到route对象
        route.setRouteImgList(routeImgList);
        //3.根据route的sid（商家id）查询商家对象
        if (route.getSid() != null && route.getSid() > 0) {
            Seller seller = sellerDao.findById(route.getSid());
            route.setSeller(seller);
        } else if (route.getSourceId() != null && route.getSourceId() > 0) {
            User user = userDao.findByUid(route.getSourceId());
            route.setUser(user);
        }


        //4. 查询收藏次数
        int count = favoriteDao.findCountByRid(route.getRid());
        routeDao.setCount(Integer.parseInt(rid), count);
        route.setCount(count);

        return route;
    }

    @Override
    public List<Route> findUserFavorite(int uid) {
        //封装PageBean
        List<Route> routes = new ArrayList<Route>();
        List<Integer> favorites = new ArrayList<Integer>();
        favorites = favoriteDao.findByUid(uid);
        for (int i = 0; i < favorites.size(); i++) {
            int rid = favorites.get(i);
            routes.add(routeDao.findOne(rid));
        }
        return routes;
    }

    @Override
    public List<Route> findUserShared(int uid) {
        List<Route> routes = new ArrayList<Route>();
        routes = routeDao.findBySourceId(uid);
        return routes;
    }

    @Override
    public boolean removeRoute(int rid) {
        List<RouteImg> routeImgs = new ArrayList<RouteImg>();
        routeImgs = routeImgDao.findByRid(rid);
        for(int i=0;i<routeImgs.size();i++){
            RouteImg routeImg = routeImgs.get(i);
            FileUtils.deleteFile(routeImg.getBigPic());
            FileUtils.deleteFile(routeImg.getSmallPic());
            routeImgDao.removeOne(routeImg.getRgid());
        }
        return routeDao.removeRoute(rid);
    }

    @Override
    public boolean update(Route route) {
        return routeDao.update(route);
    }

    @Override
    public Route add(Route r) {
        return  routeDao.add(r);
    }
}
