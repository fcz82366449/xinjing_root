package cn.easy.xinjing.service;

import java.util.Map;

import cn.easy.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.Order;
import cn.easy.xinjing.repository.OrderDao;

@Component
public class OrderService extends BaseService<Order> {
    @Autowired
    private OrderDao	orderDao;

    public Page<Order> search(Map<String, Object> searchParams, PageBean pageBean) {
        return orderDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        orderDao.delete(id);
    }

    public Order getOne(String id) {
        return orderDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Order save(Order order) {
        return orderDao.save(order);
    }

    public Order getByOutTradeNo(String outTradeNo){
        return orderDao.getByOutTradeNo(outTradeNo);
    }
}


