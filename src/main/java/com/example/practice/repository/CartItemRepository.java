package com.example.practice.repository;

import com.example.practice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>, JpaSpecificationExecutor<CartItem> {
    Optional<CartItem> findCartItemByWatchId(Long watchId);
    Optional<CartItem> findCartItemByCartId(Long cartId);
}
