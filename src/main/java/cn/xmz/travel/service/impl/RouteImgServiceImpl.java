package cn.xmz.travel.service.impl;

import cn.xmz.travel.dao.RouteImgDao;
import cn.xmz.travel.dao.impl.RouteImgDaoImpl;
import cn.xmz.travel.service.RouteImgService;

public class RouteImgServiceImpl implements RouteImgService {

    RouteImgDao routeImgDao = new RouteImgDaoImpl();
    @Override
    public void add(int rid, String s, String s1) {
        routeImgDao.add(rid, s, s1);
    }
}
