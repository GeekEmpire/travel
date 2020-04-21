package cn.xmz.travel.service.impl;

import cn.xmz.travel.dao.UserDao;
import cn.xmz.travel.dao.impl.UserDaoImpl;
import cn.xmz.travel.domain.PageBean;
import cn.xmz.travel.domain.User;
import cn.xmz.travel.service.UserService;
import cn.xmz.travel.util.MailUtils;
import cn.xmz.travel.util.UuidUtil;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public int regist(User user) {
        //1.根据用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());
        //判断u是否为null
        if(u != null){
            //用户名存在，注册失败
            return 201;
        }
        //2.保存用户信息
        //2.1设置激活码，唯一字符串
        user.setCode(UuidUtil.getUuid());
        //2.2设置激活状态
        if(user.getStatus()==null||"null".equals(user.getStatus())||user.getStatus()==""){
            user.setStatus("N");
            userDao.save(user);
            //3.激活邮件发送，邮件正文？
            String content="<a href='http://localhost/travel/user/active?code="+user.getCode()+
                    "'>欢迎注册【城市旅游网】，点击进行账号激活</a>";
            MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        }else{
            userDao.save(user);
        }
        return 200;
    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象
        User user = userDao.findByCode(code);
        if(user != null){
            //2.调用dao的修改激活状态的方法
            userDao.updateStatus(user);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 登录方法
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }

    /**
     * 修改信息
     * @param user
     * @return
     */
    @Override
    public int update(User user) {
        return userDao.updateUserInfo(user);
    }

    @Override
    public boolean changeUser(int uid) {
        return userDao.changeUser(uid);
    }

    @Override
    public PageBean<User> pageQuery(int currentPage, int pageSize, String username, String name, String sex, String telephone, String email) {

        PageBean<User> pb = new PageBean<User>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);

        //设置总记录数
        int totalCount = userDao.findTotalCount(username, name, sex, telephone, email);
        pb.setTotalCount(totalCount);
        //设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;//开始的记录数
        List<User> list = userDao.findByPage(start, pageSize, username, name, sex, telephone, email);
        pb.setList(list);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);


        return pb;
    }

}
