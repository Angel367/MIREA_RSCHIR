package com.example.egor.Repositories;

import com.example.egor.Entities.AbstractProduct;
import com.example.egor.Entities.CartItem;
import com.example.egor.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByUserAndAbstractProduct(User user, AbstractProduct abstractProduct);
    List<CartItem> findAllByUser(User user);
    List<CartItem> findAllByAbstractProduct(AbstractProduct abstractProduct);
}
