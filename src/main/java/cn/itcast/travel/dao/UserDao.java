package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public User findByUsername(String username);
    /**
     * 根据用户id查询用户信息
     * @param uid
     * @return
     */
    public User findByUid(int uid);

    /**
     * 用户保存
     * @param user
     */
    public void save(User user);

    User findByCode(String code);

    void updateStatus(User user);

    User findByUsernameAndPassword(String username, String password);

    int updateUserInfo(User user);

    boolean changeUser(int uid);
}
