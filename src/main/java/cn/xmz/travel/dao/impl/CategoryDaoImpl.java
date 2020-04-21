package cn.xmz.travel.dao.impl;

import cn.xmz.travel.dao.CategoryDao;
import cn.xmz.travel.domain.Category;
import cn.xmz.travel.domain.Route;
import cn.xmz.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Category> findAll() {
        String sql = "select * from tab_category order by cid asc";
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
    public Category add(Category c) {
        Category category = null;
        String sql = "insert into tab_category values(null,?)";
        int re = template.update(sql,c.getCname());
        if(re>=0){
            sql = "select * from tab_category where cname = ?";
            category = template.queryForObject(sql, new BeanPropertyRowMapper<Category>(Category.class), c.getCname());
        }
        return category;
    }

    @Override
    public List<Category> findBySearch(int id, String cname) {
        String sql = " select * from tab_category where 1 = 1 ";
        //1.定义sql模板
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(id != 0){
            sb.append( " and cid = ? ");
            params.add(id);//添加？对应的值
        }
        if(cname != null && cname.length() > 0&&!"null".equals(cname)){
            sb.append(" and cname like ? ");

            params.add("%"+cname+"%");
        }
        sql = sb.toString();

        return template.query(sql,new BeanPropertyRowMapper<>(Category.class),params.toArray());
    }
}
