package cn.xmz.travel.service;

import cn.xmz.travel.domain.PageBean;
import cn.xmz.travel.domain.User;

public interface UserService {
    /**
     * 注册用户
     * @param user
     * @return
     */
    int regist(User user);

    boolean active(String code);

    User login(User user);

    int update(User user);

    boolean changeUser(int uid);

    PageBean<User> pageQuery(int currentPage, int pageSize, String username, String name, String sex, String telephone, String email);
}
