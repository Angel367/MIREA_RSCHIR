package com.example.egor.Controllers;
import com.example.egor.Controllers.DTO.CartItemDTO;
import com.example.egor.Entities.AbstractProduct;
import com.example.egor.Entities.CartItem;
import com.example.egor.Entities.User;
import com.example.egor.Repositories.AbstractProductRepository;
import com.example.egor.Repositories.UserRepository;
import com.example.egor.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private AbstractProductRepository abstractProductRepository;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam long userId, @RequestParam Long productId,
                                            @RequestParam int quantity) {
        if (userRepository.existsById(userId) && abstractProductRepository.existsById(productId)) {
            User user = userRepository.getById(userId);
            AbstractProduct abstractProduct = abstractProductRepository.getById(productId);
            if (!cartService.addToCart(user, abstractProduct, quantity)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Товар закончился");
            }
            return ResponseEntity.ok("Товар успешно добавлен в корзину.");
        } else return ResponseEntity.notFound().build();

    }
    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam long userId, @RequestParam Long productId) {
        if (userRepository.existsById(userId) && abstractProductRepository.existsById(productId)) {
            User user = userRepository.getById(userId);
            AbstractProduct abstractProduct = abstractProductRepository.getById(productId);
            if (!cartService.removeFromCart(user, abstractProduct))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого товара в корзине не существует");
            return ResponseEntity.ok("Товар успешно удален из корзины.");
        } else return ResponseEntity.notFound().build();
    }
    @PostMapping("/update")
    public ResponseEntity<String> updateCartItemQuantity(@RequestParam long userId, @RequestParam Long productId,
                                                         @RequestParam int newQuantity) {
        if (userRepository.existsById(userId) && abstractProductRepository.existsById(productId)) {
            User user = userRepository.getById(userId);
            AbstractProduct abstractProduct = abstractProductRepository.getById(productId);
            if (!cartService.updateCartItemQuantity(user, abstractProduct, newQuantity)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Товар закончился");
            }
            return ResponseEntity.ok("Количество товара в корзине обновлено.");
        } else return ResponseEntity.notFound().build();
    }
    @GetMapping("/view")
    public ResponseEntity<List<CartItemDTO>> viewCart(@RequestParam long userId) {
        if (userRepository.existsById(userId)) {
            User user = userRepository.getById(userId);
            List<CartItem> cartItems = cartService.getCartItems(user);
            List<CartItemDTO> cartItemDTOs = cartItems.stream()
                    .map(cartItem -> new CartItemDTO(cartItem.getId(), cartItem.getAbstractProduct().getId(),
                            cartItem.getAbstractProduct().getName(), cartItem.getAbstractProduct().getPrice(),
                            cartItem.getQuantity(), (long) user.getId(), user.getUsername())).toList();
            return ResponseEntity.ok(cartItemDTOs);
        } else return ResponseEntity.notFound().build();
    }
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestParam long userId) {
        if (userRepository.existsById(userId)) {
            User user = userRepository.getById(userId);
            if (cartService.checkout(user) != null)
                return ResponseEntity.status(HttpStatus.CONFLICT).body(cartService.checkout(user));
            else {
                return ResponseEntity.ok("Заказ успешно оформлен.");
            }
        } else return ResponseEntity.notFound().build();
    }
}
