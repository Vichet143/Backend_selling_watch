package com.example.practice.service.impl;

import com.example.practice.dto.OrderRequestDTO;
import com.example.practice.entity.*;
import com.example.practice.exception.ApiException;
import com.example.practice.mapper.OrderMapper;
import com.example.practice.repository.OrderDetailRepository;
import com.example.practice.repository.OrderRepository;
import com.example.practice.repository.WatchRepository;
import com.example.practice.service.CartService;
import com.example.practice.service.InventoryService;
import com.example.practice.service.OrderService;
import com.example.practice.service.UserService;
import com.example.practice.service.util.PageUtil;
import com.example.practice.spec.PageFilter;
import com.example.practice.spec.PageSpec;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final WatchRepository watchRepository;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final CartService cartService;
    private final InventoryService inventoryService;

    @Transactional
    @Override
    public Order create(OrderRequestDTO orderRequestDTO) {

        Cart cart = cartService.findByUserId(orderRequestDTO.getUserId());

        if (cart == null) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "false",
                    "Cart is empty"
            );
        }

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "false",
                    "Cart has no items"
            );
        }

        Order order = orderMapper.toOrder(orderRequestDTO);

        order.setUser(cart.getUser());
        order.setShoppingAddress(orderRequestDTO.getShippingAddress());
        order.setPhoneNumber(orderRequestDTO.getPhoneNumber());
        order.setOrderStatus(String.valueOf(OrderStatus.Pending));
        order.setPaymentStatus(String.valueOf(PaymentStatus.Pending));
        order.setPrice(BigDecimal.ZERO);

        // Save order first
        order = orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {

            Watch watch = cartItem.getWatch();

            if (watch == null) {
                continue;
            }

            BigDecimal price = watch.getPrice();
            Integer quantity = cartItem.getQuantity();

            if (price == null || quantity == null || quantity <= 0) {
                continue;
            }

            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setOrder(order);
            orderDetail.setWatch(watch);
            orderDetail.setPrice(price);
            orderDetail.setQuantity(quantity);

            Inventory inventory = inventoryService.findByWatchId(watch.getId());
            int remainingStock = inventory.getQuantity() - quantity;
            if (remainingStock < 0) {
                remainingStock = 0; // safety guard against overselling
            }
            inventory.setQuantity(remainingStock);

            if (remainingStock == 0) {
                watch.setStatus(WatchStatus.SOLD_OUT);
            }
            watchRepository.save(watch);
            orderDetailRepository.save(orderDetail);

            totalPrice = totalPrice.add(
                    price.multiply(BigDecimal.valueOf(quantity))
            );
        }

        order.setPrice(totalPrice);

        return orderRepository.save(order);
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", "order id is not found with id = " + id));
    }

    @Override
    public Order updateById(Long id, Order order) {
        try{
            Order order2 = findById(id);
            if (order.getPhoneNumber() != null){
                order2.setPhoneNumber(order.getPhoneNumber());
            }
            if (order.getShoppingAddress() != null){
                order2.setShoppingAddress(order.getShoppingAddress());
            }
            if (order.getPaymentStatus() != null){
                order2.setPaymentStatus(order.getPaymentStatus());
            }

            if (order.getOrderStatus() != null){
                order2.setOrderStatus(order.getOrderStatus());
            }


            return orderRepository.save(order2);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false", e.getMessage());
        }

    }

    @Override
    public void deleteById(Long id) {
        Order byId = findById(id);
        orderRepository.deleteById(byId.getId());
    }

    @Override
    public Page<Order> getAll(Map<String, String> param) {
        PageFilter pageFilter = new PageFilter();

        if (param.containsKey("id")) {
            pageFilter.setId(Long.parseLong(param.get("id")));
            findById(pageFilter.getId());
        }

        PageSpec<Order> pageSpec = new PageSpec<>();

        pageSpec.equal(
                "id",
                pageFilter.getId()
        );

        int pageLimit = PageUtil.DEFAULT_PAGE_LIMIT;
        if (param.containsKey( PageUtil.PAGE_LIMIT)){
            pageLimit = Integer.parseInt(param.get(PageUtil.PAGE_LIMIT));

        }

        int pageNumber = PageUtil.DEFAULT_PAGE_NUMBER;
        if (param.containsKey( PageUtil.PAGE_SIZE)){
            pageNumber = Integer.parseInt(param.get(PageUtil.PAGE_SIZE));

        }

        Pageable pageable = PageUtil.getPageable(pageNumber, pageLimit);

        return orderRepository.findAll(pageSpec, pageable);
    }
}
