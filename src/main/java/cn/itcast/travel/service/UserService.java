package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

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
}
