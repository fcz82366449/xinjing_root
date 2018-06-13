package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Order;

public interface OrderDao extends PagingAndSortingRepository<Order, String>, JpaSpecificationExecutor<Order> {
    Order getByOutTradeNo(String outTradeNo);
}
