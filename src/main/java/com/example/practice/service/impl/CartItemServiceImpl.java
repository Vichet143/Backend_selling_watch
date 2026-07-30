package com.example.practice.service.impl;


import com.example.practice.dto.CartItemRequestDTO;
import com.example.practice.entity.Cart;
import com.example.practice.entity.CartItem;
import com.example.practice.entity.Category;
import com.example.practice.entity.Watch;
import com.example.practice.exception.ApiException;
import com.example.practice.mapper.CartItemMapper;
import com.example.practice.repository.CartItemRepository;
import com.example.practice.service.CartItemService;
import com.example.practice.service.CartService;
import com.example.practice.service.WatchService;
import com.example.practice.service.util.PageUtil;
import com.example.practice.spec.PageFilter;
import com.example.practice.spec.PageSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final CartService cartService;
    private final WatchService watchService;

    @Override
    public CartItem create(CartItemRequestDTO cartItemRequestDTO) {
        Cart cart = cartService.findById(cartItemRequestDTO.getCartId());
        Watch watch = watchService.findById(cartItemRequestDTO.getWatchId());

        CartItem cartItem = cartItemMapper.toCartItem(cartItemRequestDTO);

        if (cartItemRepository.findCartItemByWatchId(watch.getId()).isPresent()) {

            //make update quantity when I have already in cart item
            List<CartItem> all = cartItemRepository.findAll();
            CartItem cartItem3 = all.stream()
                    .filter(e -> e.getWatch().getId().equals(watch.getId()))
                    .findFirst()
                    .orElse(null);
            assert cartItem3 != null;

            CartItem cartItem1 = findById(cartItem3.getId());

            cartItem1.setQuantity(cartItem1.getQuantity() + cartItemRequestDTO.getQuantity());
            return cartItemRepository.save(cartItem1);
        }
        cartItem.setCart(cart);
        cartItem.setWatch(watch);
        cartItem.setPrice(watch.getPrice());
        cartItem.setStatus(true);

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem findById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", "Cart Item not found with id " + id));
    }

    @Override
    public CartItem updateById(Long id, CartItemRequestDTO cartItemRequestDTO) {
        CartItem cartItem = findById(id);
        if (cartItemRequestDTO.getCartId() != null) {
            Cart cart = cartService.findById(cartItemRequestDTO.getCartId());
            cartItem.setCart(cart);
        }

        if (cartItemRequestDTO.getWatchId() != null) {
            Watch watch = watchService.findById(cartItemRequestDTO.getWatchId());
            cartItem.setWatch(watch);
        }

        if (cartItemRequestDTO.getQuantity() != null) {
            if (cartItemRequestDTO.getQuantity() == 0) {
                deleteById(cartItem.getId());
                return null;
            }
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDTO.getQuantity());
        }

        return cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteById(Long id) {
        CartItem byId = findById(id);
        cartItemRepository.deleteById(byId.getId());
    }

    @Override
    public CartItem findByCartId(Long id) {
        return cartItemRepository.findCartItemByCartId(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "false", "Cart id is not found with id " + id));
    }

    @Override
    public Page<CartItem> getAllCartItem(Map<String, String> param) {
        PageFilter pageFilter = new PageFilter();

        if (param.containsKey("id")) {
            pageFilter.setId(Long.parseLong(param.get("id")));
            findById(pageFilter.getId());
        }

        if (param.containsKey("cartId")){
            pageFilter.setCartId(Long.parseLong(param.get("cartId")));
        }

        PageSpec<CartItem> pageSpec = new PageSpec<>();

        pageSpec.equal(
                "id",
                pageFilter.getId()
        );
//
//        pageSpec.equal(
//                "cartId",
//                pageFilter.getCartId()
//        );

        pageSpec.equalJoin("cart", "id", pageFilter.getCartId());

        int pageLimit = PageUtil.DEFAULT_PAGE_LIMIT;
        if (param.containsKey( PageUtil.PAGE_LIMIT)){
            pageLimit = Integer.parseInt(param.get(PageUtil.PAGE_LIMIT));

        }

        int pageNumber = PageUtil.DEFAULT_PAGE_NUMBER;
        if (param.containsKey( PageUtil.PAGE_SIZE)){
            pageNumber = Integer.parseInt(param.get(PageUtil.PAGE_SIZE));

        }

        Pageable pageable = PageUtil.getPageable(pageNumber, pageLimit);

        return cartItemRepository.findAll(pageSpec, pageable);
    }
}
