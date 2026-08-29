package com.example.practice.controller;

import com.example.practice.dto.OrderRequestDTO;
import com.example.practice.dto.PageDTO;
import com.example.practice.dto.ResponseMessageDTO;
import com.example.practice.entity.CartItem;
import com.example.practice.entity.Order;
import com.example.practice.entity.OrderDetail;
import com.example.practice.entity.Watch;
import com.example.practice.exception.ApiException;
import com.example.practice.repository.OrderDetailRepository;
import com.example.practice.service.OrderService;
import com.example.practice.service.TelegramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final TelegramService telegramService;
    private final OrderDetailRepository orderDetailRepository;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {

        Order order = orderService.create(orderRequestDTO);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "User make order",
                order
        );

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        List<OrderDetail> orderDetails =
                orderDetailRepository.findByOrderId(order.getId());

        StringBuilder items = new StringBuilder();

        int index = 1;

        for (OrderDetail detail : orderDetails) {

            Watch watch = detail.getWatch();

            BigDecimal subtotal =
                    detail.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(detail.getQuantity())
                            );

            items.append("""
                
                %d. ⌚ %s
                   Price: $%s
                   Qty: %d
                   Subtotal: $%s
                """.formatted(
                    index++,
                    watch.getName(),
                    detail.getPrice(),
                    detail.getQuantity(),
                    subtotal
            ));
        }

        String message = """
            🛍️ NEW ORDER
            
            ━━━━━━━━━━━━━━━━━━
            📦 Order ID: #%d
            👤 Customer: %s
            📞 Phone: %s
            📍 Address: %s
            ━━━━━━━━━━━━━━━━━━
            
            🛒 ITEMS
            %s
            ━━━━━━━━━━━━━━━━━━
            💰 Total: $%s
            💳 Payment: %s
            📋 Status: %s
            📅 Date: %s
            ━━━━━━━━━━━━━━━━━━
            """.formatted(
                order.getId(),
                order.getUser().getUserName(),
                order.getPhoneNumber(),
                order.getShoppingAddress(),
                items,
                order.getPrice(),
                order.getPaymentStatus(),
                order.getOrderStatus(),
                order.getDate().format(formatter)
        );

        telegramService.sendMessage(message);
        return ResponseEntity.ok().body(responseMessageDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Order order){

        Order order1 = orderService.updateById(id, order);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Update data order success",
                order1
        );

        return ResponseEntity.ok().body(responseMessageDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        orderService.deleteById(id);

        ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                true,
                "Delete order success",
                "Delete with id = " + id
        );

        return ResponseEntity.ok().body(responseMessageDTO);
    }

    @GetMapping
    public ResponseEntity<?> getAllCartItem(@RequestParam Map<String, String> param){
        try{
            Page<Order> allCartItem = orderService.getAll(param);

            PageDTO pageDTO = new PageDTO(allCartItem);

            ResponseMessageDTO<?> responseMessageDTO = new ResponseMessageDTO<>(
                    true,
                    "Get data cart item",
                    pageDTO
            );


            return ResponseEntity.ok().body(responseMessageDTO);
        }catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "false", e.getMessage());
        }

    }
}
