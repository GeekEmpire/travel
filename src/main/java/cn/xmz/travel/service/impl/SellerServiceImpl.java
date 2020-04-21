package cn.xmz.travel.service.impl;

import cn.xmz.travel.dao.SellerDao;
import cn.xmz.travel.dao.impl.SellerDaoImpl;
import cn.xmz.travel.domain.PageBean;
import cn.xmz.travel.domain.Seller;
import cn.xmz.travel.service.SellerService;

import java.util.List;

public class SellerServiceImpl implements SellerService {

    SellerDao sellerDao = new SellerDaoImpl();

    @Override
    public int regist(Seller seller) {
        //1.根据用户名查询用户对象
        Seller s = sellerDao.findBySname(seller.getSname());
        //判断u是否为null
        if(s != null){
            //用户名存在，注册失败
            return 201;
        }
        //2.保存用户信息
        //2.1设置激活码，唯一字符串
        sellerDao.save(seller);
        return 200;
    }

    @Override
    public Seller findOne(int sid) {
        return sellerDao.findById(sid);
    }

    @Override
    public int update(Seller seller) {
        return sellerDao.update(seller);
    }

    @Override
    public boolean remove(int id) {
        return sellerDao.remove(id);
    }

    @Override
    public PageBean<Seller> pageQuery(int currentPage, int pageSize) {
        PageBean<Seller> pb = new PageBean<Seller>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);

        //设置总记录数
        int totalCount = sellerDao.findTotalCount();
        pb.setTotalCount(totalCount);
        //设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;//开始的记录数
        List<Seller> list = sellerDao.findByPage(start, pageSize);
        pb.setList(list);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);


        return pb;
    }

    @Override
    public List<Seller> findAll() {
        return sellerDao.findAll();
    }
}
