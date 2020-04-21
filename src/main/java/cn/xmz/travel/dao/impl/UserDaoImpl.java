package cn.xmz.travel.dao.impl;

import cn.xmz.travel.dao.UserDao;
import cn.xmz.travel.domain.User;
import cn.xmz.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            //1.定义sql
            String sql = "select * from tab_user where username = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (Exception e) {

        }

        return user;
    }

    @Override
    public User findByUid(int uid) {
        User user = null;
        try {
            //1.定义sql
            String sql = "select * from tab_user where uid = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), uid);
        } catch (Exception e) {

        }

        return user;
    }

    @Override
    public void save(User user) {
        //1.定义sql
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        //2.执行sql

        template.update(sql,user.getUsername(),
                    user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode()
                );
    }

    /**
     * 根据激活码查询用户对象
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {
        User user = null;
        try {
            String sql = "select * from tab_user where code = ?";

            user = template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),code);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 修改指定用户激活状态
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        String sql = " update tab_user set status = 'Y' where uid=?";
        template.update(sql,user.getUid());
    }

    /**
     * 根据用户名和密码查询的方法
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = null;
        try {
            //1.定义sql
            String sql = "select * from tab_user where username = ? and password = ?";
            //2.执行sql
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username,password);
        } catch (Exception e) {

        }

        return user;
    }


    /**
     * 修改个人信息
     * @param user
     * @return
     */
    @Override
    public int updateUserInfo(User user) {
        String sql = "update tab_user set password=?, name=?, birthday=?, sex=?, telephone=?, email=?, status=?" +
                " where uid=?";
        int re= template.update(sql, user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getUid()
        );
        if(re>0) return 200;
        else return 201;
    }

    /**
     * 禁用账号
     * @param uid
     * @return
     */
    @Override
    public boolean changeUser(int uid) {
        String sql = " update tab_user set status = 'F' where uid=?";
        int re = template.update(sql,uid);
        if(re>=0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int findTotalCount(String username, String name, String sex, String telephone, String email) {
        //String sql = "select count(*) from tab_route where cid = ?";
        //1.定义sql模板
        String sql = "select count(*) from tab_user where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(username != null && username.length() > 0){
            sb.append(" and username like ? ");
            params.add("%"+username+"%");
        }

        if(name != null && name.length() > 0){
            sb.append(" and name like ? ");
            params.add("%"+name+"%");
        }

        if(sex != null && sex.length()==1){
            sb.append(" and sex = ? ");
            params.add(sex);
        }

        if(telephone != null && telephone.length() > 0){
            sb.append(" and telephone like ? ");
            params.add("%"+telephone+"%");
        }

        if(email != null && email.length() > 0){
            sb.append(" and email like ? ");
            params.add("%"+email+"%");
        }

        sql = sb.toString();
        return template.queryForObject(sql,Integer.class,params.toArray());
    }

    @Override
    public List<User> findByPage(int start, int pageSize, String username, String name, String sex, String telephone, String email) {
         /*String sql = "select * from tab_route where 1=1 and cid = ? and rname like ?
        and order by count asc and price>? and price<? limit ? , ?";
         */
        String sql = " select * from tab_user where 1 = 1 ";
        //1.定义sql模板
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(username != null && username.length() > 0){
            sb.append(" and username like ? ");
            params.add("%"+username+"%");
        }

        if(name != null && name.length() > 0){
            sb.append(" and name like ? ");
            params.add("%"+name+"%");
        }

        if(sex != null && sex.length()==1){
            sb.append(" and sex = ? ");
            params.add(sex);
        }

        if(telephone != null && telephone.length() > 0){
            sb.append(" and telephone like ? ");
            params.add("%"+telephone+"%");
        }

        if(email != null && email.length() > 0){
            sb.append(" and email like ? ");
            params.add("%"+email+"%");
        }

        sb.append(" limit ? , ? ");//分页条件

        sql = sb.toString();

        params.add(start);
        params.add(pageSize);

        return template.query(sql,new BeanPropertyRowMapper<>(User.class),params.toArray());
    }
}
