package cn.xmz.travel.dao.impl;

import cn.xmz.travel.dao.FavoriteDao;
import cn.xmz.travel.domain.Favorite;
import cn.xmz.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = " select * from tab_favorite where rid = ? and uid = ?";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
            return null;
        }
        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {
        String sql = "SELECT COUNT(*) FROM tab_favorite WHERE rid = ?";

        return template.queryForObject(sql,Integer.class,rid);
    }

    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";

        template.update(sql,rid,new Date(),uid);
    }

    @Override
    public void remove(int i, int uid) {
        String sql = "delete from tab_favorite where rid=? && uid = ?";
        template.update(sql, i, uid);
    }

    @Override
    public List<Integer> findByUid(int uid) {
        String sql = "select rid from tab_favorite where uid=?";
        return template.queryForList(sql, Integer.class, uid);
    }
}
