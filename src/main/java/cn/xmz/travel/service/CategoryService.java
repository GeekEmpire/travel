package cn.xmz.travel.service;

import cn.xmz.travel.domain.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> findAll();

    boolean remove(int cid);

    boolean update(String cname, int cid);

    boolean add(Category c);

    List<Category> findBySearch(int id, String cname);
}
