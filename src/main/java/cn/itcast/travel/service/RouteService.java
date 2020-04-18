package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

/**
 * 线路Service
 */
public interface RouteService {

    /**
     * 根据类别进行分页查询
     * @param cid
     * @param currentPage
     * @param pageSize
     * @param rid
     * @return
     */
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname,
                                     boolean crank, int beginPrice, int endPrice, int rid);

    /**
     * 根据id查询
     * @param rid
     * @return
     */
    public Route findOne(String rid);

    /**
     * 根据用户id查询收藏
     * @param uid
     * @return
     */
    public List<Route> findUserFavorite(int uid);

    List<Route> findUserShared(int uid);

    boolean removeRoute(int rid);

    boolean update(Route route);
}
