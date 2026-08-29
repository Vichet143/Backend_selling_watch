package com.example.practice.service;


import com.example.practice.dto.OrderRequestDTO;
import com.example.practice.entity.Order;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface OrderService {
    Order create(OrderRequestDTO orderRequestDTO);
    Order findById(Long id);
    Order updateById(Long id, Order order);
    void deleteById(Long id);
    Page<Order> getAll(Map<String,String> param);
}
