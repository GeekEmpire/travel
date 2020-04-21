package cn.xmz.travel.dao.impl;

import cn.xmz.travel.dao.SellerDao;
import cn.xmz.travel.domain.Seller;
import cn.xmz.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class SellerDaoImpl implements SellerDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Seller findById(int id) {

        String sql = "select * from tab_seller where sid = ? ";
        return template.queryForObject(sql,new BeanPropertyRowMapper<Seller>(Seller.class),id);
    }

    @Override
    public int update(Seller seller) {
        String sql = "update tab_seller set sname=?, consphone=?, address=? where sid=?";
        int re= template.update(sql, seller.getSname(),
                seller.getConsphone(),
                seller.getAddress(),
                seller.getSid()
        );
        if(re>0) return 200;
        else return 201;
    }

    @Override
    public boolean remove(int id) {
        String sql = "delete from tab_seller where sid= ?";
        int re = template.update(sql, id);
        if(re>=0){
            return true;
        }
        return false;
    }

    @Override
    public int findTotalCount() {
        String sql = "select count(*) from tab_seller";
        return template.queryForObject(sql,Integer.class);
    }

    @Override
    public List<Seller> findByPage(int start, int pageSize) {
        String sql = " select * from tab_seller limit ? , ? ";//分页条件
        return template.query(sql,new BeanPropertyRowMapper<>(Seller.class),
                start, pageSize);
    }

    @Override
    public void save(Seller seller) {
        //1.定义sql
        String sql = "insert into tab_seller(sname,consphone,address) values(?,?,?)";
        //2.执行sql
        template.update(sql,seller.getSname()
                , seller.getConsphone()
                , seller.getAddress()
        );
    }

    @Override
    public Seller findBySname(String sname) {
        Seller seller = null;
        try {
            //1.定义sql
            String sql = "select * from tab_seller where sname=?";
            //2.执行sql
            seller = template.queryForObject(sql, new BeanPropertyRowMapper<Seller>(Seller.class), sname);
        } catch (Exception e) {

        }

        return seller;
    }

    @Override
    public List<Seller> findAll() {
        //1.定义sql
        String sql = "select * from tab_seller";
        //2.执行sql
        return template.query(sql,new BeanPropertyRowMapper<Seller>(Seller.class));
    }
}
