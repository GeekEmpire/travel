package cn.xmz.travel.dao.impl;

import cn.xmz.travel.dao.RouteDao;
import cn.xmz.travel.domain.Route;
import cn.xmz.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCount(int cid, String rname, int beginPrice, int endPrice, int rid) {
        //String sql = "select count(*) from tab_route where cid = ?";
        //1.定义sql模板
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");

            params.add(cid);//添加？对应的值
        }
        if(rid != 0){
            sb.append( " and rid = ? ");

            params.add(rid);//添加？对应的值
        }

        if(rname != null && rname.length() > 0){
            sb.append(" and rname like ? ");

            params.add("%"+rname+"%");
        }

        if(beginPrice > 0){
            sb.append(" and price>?");
            params.add(beginPrice);
        }
        if(endPrice > 0){
            sb.append(" and price < ?");
            params.add(endPrice);
        }

        sql = sb.toString();


        return template.queryForObject(sql,Integer.class,params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname, boolean crank, int beginPrice, int endPrice, int rid) {
        /*String sql = "select * from tab_route where 1=1 and cid = ? and rname like ?
        and order by count asc and price>? and price<? limit ? , ?";
         */
        String sql = " select * from tab_route where 1 = 1 ";
        //1.定义sql模板
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");

            params.add(cid);//添加？对应的值
        }
        if(rid != 0){
            sb.append( " and rid = ? ");

            params.add(rid);//添加？对应的值
        }

        if(rname != null && rname.length() > 0){
            sb.append(" and rname like ? ");

            params.add("%"+rname+"%");
        }
        if(beginPrice > 0){
            sb.append(" and price>?");
            params.add(beginPrice);
        }
        if(endPrice > 0){
            sb.append(" and price < ?");
            params.add(endPrice);
        }
        if(crank){
            sb.append(" order by count desc");
        }
        sb.append(" limit ? , ? ");//分页条件

        sql = sb.toString();

        params.add(start);
        params.add(pageSize);

        return template.query(sql,new BeanPropertyRowMapper<>(Route.class),params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<>(Route.class),rid);
    }

    @Override
    public int setCount(int rid, int count) {
        String sql = "update tab_route set count=? where rid = ?";
        return template.update(sql, count, rid);
    }

    @Override
    public List<Route> findBySourceId(int uid) {
        String sql = "select * from tab_route where sourceId=?";
        return template.query(sql,new BeanPropertyRowMapper<>(Route.class),uid);
    }

    @Override
    public boolean removeRoute(int rid) {
        String sql = "delete from tab_route where rid=?";
        int re = template.update(sql, rid);
        if(re>0)
            return true;
        else
            return false;
    }

    @Override
    public boolean update(Route route) {
        String sql = "update tab_route set rname=?, price=?, routeIntroduce=?, cid=?, rimage=?, detail=?" +
                " where rid=?";
        int re= template.update(sql, route.getRname(),
            route.getPrice(),
            route.getRouteIntroduce(),
            route.getCid(),
            route.getRimage(),
            route.getDetail(),
            route.getRid()
        );
        if(re>0) return true;
        return false;
    }

    @Override
    public Route add(Route r) {
        String sql = "";
        int re = 0;
        if(r.getSid()!=null&&r.getSid()!=0) {
            sql = "insert into tab_route(rname,price,rflag,routeIntroduce,rdate,cid,rimage,sid,detail) " +
                    "values(?,?,?,?,?,?,?,?,?)";
            re = template.update(sql, r.getRname(),
                    r.getPrice(),
                    r.getRflag(),
                    r.getRouteIntroduce(),
                    r.getRdate(),
                    r.getCid(),
                    r.getRimage(),
                    r.getSid(),
                    r.getDetail()
            );
        }
        if(r.getSourceId()!=null&&r.getSourceId()!=0){
            sql = "insert into tab_route(rname,price,rflag,routeIntroduce,rdate,cid,rimage,sourceId,detail) " +
                    "values(?,?,?,?,?,?,?,?,?)";
            re = template.update(sql, r.getRname(),
                    r.getPrice(),
                    r.getRflag(),
                    r.getRouteIntroduce(),
                    r.getRdate(),
                    r.getCid(),
                    r.getRimage(),
                    r.getSourceId(),
                    r.getDetail()
            );
        }
        //2.执行sql

        Route route = null;

        if(re>=0){
            sql = "select * from tab_route where rname=? and routeIntroduce =?";
            System.out.println(sql);
            route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), r.getRname(),r.getRouteIntroduce());
        }
        return  route;
    }


}
