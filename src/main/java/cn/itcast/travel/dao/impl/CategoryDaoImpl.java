package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Category> findAll() {
        String sql = "select * from tab_category ";
        return template.query(sql,new BeanPropertyRowMapper<Category>(Category.class));
    }

    @Override
    public boolean remove(int cid) {
        String sql = "delete from tab_category where cid = ? ";
        int re = template.update(sql, cid);
        if(re>=0) return true;
        else return false;
    }

    @Override
    public boolean update(String cname, int cid) {
        String sql = "update tab_category set cname=? where cid =?";
        int re = template.update(sql, cname, cid);
        if(re>=0) return true;
        else return false;
    }

    @Override
    public boolean add(Category c) {
        String sql = "insert into tab_category values(null,?)";
        int re = template.update(sql,c.getCname());
        if(re>=0) return true;
        else return false;
    }
}
