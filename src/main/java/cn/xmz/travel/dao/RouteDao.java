package cn.xmz.travel.dao;

import cn.xmz.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    /**
     * 根据cid查询总记录数
     */
    public int findTotalCount(int cid, String rname, int beginPrice, int endPrice, int rid);

    /**
     * 根据cid，start,pageSize查询当前页的数据集合
     */
    public List<Route> findByPage(int cid, int start, int pageSize, String rname, boolean crank, int beginPrice, int endPrice, int rid);

    /**
     * 根据id查询
     * @param rid
     * @return
     */
    public Route findOne(int rid);

    public int setCount(int rid, int count);

    List<Route> findBySourceId(int uid);

    boolean removeRoute(int rid);

    boolean update(Route route);

    Route add(Route r);
}
