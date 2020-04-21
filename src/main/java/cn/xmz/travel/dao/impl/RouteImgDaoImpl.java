package cn.xmz.travel.dao.impl;

import cn.xmz.travel.dao.RouteImgDao;
import cn.xmz.travel.domain.RouteImg;
import cn.xmz.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RouteImgDaoImpl implements RouteImgDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());


    @Override
    public List<RouteImg> findByRid(int rid) {
        String sql = "select * from tab_route_img where rid = ? ";
        return template.query(sql,new BeanPropertyRowMapper<RouteImg>(RouteImg.class),rid);
    }

    @Override
    public boolean removeOne(int rgid) {
        int re =-1;
        String sql = "delete from tab_route_img where rgid = ?";
        re = template.update(sql, rgid);
        if(re>=0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void add(int rid, String s, String s1) {
        String sql = "insert into tab_route_img (rid, bigPic, smallPic) values(?,?,?)";
        template.update(sql, rid, s, s1);
    }
}
