package com.example.egor.Services;

import com.example.egor.Repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.egor.Entities.*;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;
    private boolean isProductOverSold(AbstractProduct product, int quantity) {
        int currQuantity = 0;
        for (CartItem cartItem : cartItemRepository.findAllByAbstractProduct(product)) {
            currQuantity += cartItem.getQuantity();
        }
        return currQuantity + quantity > product.getQuantity();
    }
    private boolean isProductOverSold(AbstractProduct product, int quantity, int quantityForUpdatingOp) {
        int currQuantity = 0;
        for (CartItem cartItem : cartItemRepository.findAllByAbstractProduct(product)) {
            currQuantity += cartItem.getQuantity();
        }
        return currQuantity - quantityForUpdatingOp + quantity > product.getQuantity();
    }
    public boolean addToCart(Client client, AbstractProduct product, int quantity) {
        CartItem cartItem = cartItemRepository.findByClientAndAbstractProduct(client, product);
        if (isProductOverSold(product, quantity)) return false;
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setClient(client);
            cartItem.setAbstractProduct(product);
            cartItem.setQuantity(quantity);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItemRepository.save(cartItem);
        return true;
    }
    public boolean removeFromCart(Client client, AbstractProduct product) {
        // Проверяем, есть ли товар в корзине клиента
        CartItem cartItem = cartItemRepository.findByClientAndAbstractProduct(client, product);

        if (cartItem != null) {
            // Если товар найден в корзине, удаляем его
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }
    public boolean updateCartItemQuantity(Client client, AbstractProduct product, int newQuantity) {
        // Проверяем, есть ли товар в корзине клиента
        CartItem cartItem = cartItemRepository.findByClientAndAbstractProduct(client, product);
        if (cartItem != null) {
            if (isProductOverSold(product, newQuantity, cartItem.getQuantity())) return false;
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }
        return true;
    }
    public List<CartItem> getCartItems(Client client) {
        return cartItemRepository.findAllByClient(client);
    }
    public String checkout(Client client) {
        List<CartItem> cartItems = cartItemRepository.findAllByClient(client);
        if (cartItems.isEmpty()) {
            return "Корзины не существует!";
        }
        for (CartItem cartItem : cartItems) {
            if (isProductOverSold(cartItem.getAbstractProduct(), 0)) {
                return "Продукт с id = " + cartItem.getId().toString() + " закончился.";
            }
            cartItem.getAbstractProduct().setQuantity(
                    cartItem.getAbstractProduct().getQuantity()-cartItem.getQuantity());
        }
        cartItemRepository.deleteAll(cartItems);
        return null;
    }
}

