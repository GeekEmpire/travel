package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.ManagerDao;
import cn.itcast.travel.dao.impl.ManagerDaoImpl;
import cn.itcast.travel.domain.Manager;
import cn.itcast.travel.service.ManagerService;

public class ManagerServiceImpl implements ManagerService {

    ManagerDao managerDao = new ManagerDaoImpl();
    @Override
    public Manager login(Manager manager) {
        return managerDao.login(manager);
    }
}
