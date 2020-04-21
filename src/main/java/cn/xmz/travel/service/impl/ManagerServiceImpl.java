package cn.xmz.travel.service.impl;

import cn.xmz.travel.dao.ManagerDao;
import cn.xmz.travel.dao.impl.ManagerDaoImpl;
import cn.xmz.travel.domain.Manager;
import cn.xmz.travel.service.ManagerService;

public class ManagerServiceImpl implements ManagerService {

    ManagerDao managerDao = new ManagerDaoImpl();
    @Override
    public Manager login(Manager manager) {
        return managerDao.login(manager);
    }
}
