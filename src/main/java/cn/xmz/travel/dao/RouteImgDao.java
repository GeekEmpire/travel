package cn.xmz.travel.dao;

import cn.xmz.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {

    /**
     * 根据route的id查询图片
     * @param rid
     * @return
     */
    public List<RouteImg> findByRid(int rid);

    boolean removeOne(int rgid);

    void add(int rid, String s, String s1);
}
