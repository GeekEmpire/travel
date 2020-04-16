package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.ManagerDao;
import cn.itcast.travel.domain.Manager;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class ManagerDaoImpl implements ManagerDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Manager login(Manager manager) {
        Manager m = null;
        try {
            //1.定义sql
            String sql = "select * from tab_manager where mname = ? and password = ?";
            //2.执行sql
            m = template.queryForObject(sql, new BeanPropertyRowMapper<Manager>(Manager.class),
                    manager.getMname(),manager.getPassword());
        } catch (Exception e) {

        }
        return  m;
    }
}
