package com.example.egor.Repositories;

import com.example.egor.Entities.AbstractProduct;
import com.example.egor.Entities.CartItem;
import com.example.egor.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByClientAndAbstractProduct(Client client, AbstractProduct abstractProduct);
    List<CartItem> findAllByClient(Client client);
    List<CartItem> findAllByAbstractProduct(AbstractProduct abstractProduct);
}
