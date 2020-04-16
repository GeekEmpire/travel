package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;

import java.util.List;

public interface CategoryDao {
    /**
     * 查询所有
     * @return
     */
    public List<Category> findAll();

    boolean remove(int cid);

    boolean update(String cname, int cid);

    boolean add(Category c);
}
