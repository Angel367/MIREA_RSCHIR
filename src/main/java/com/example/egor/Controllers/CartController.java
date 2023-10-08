package com.example.egor.Controllers;
import com.example.egor.Controllers.DTO.CartItemDTO;
import com.example.egor.Entities.AbstractProduct;
import com.example.egor.Entities.CartItem;
import com.example.egor.Entities.Client;
import com.example.egor.Repositories.AbstractProductRepository;
import com.example.egor.Repositories.ClientRepository;
import com.example.egor.Services.CartService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private AbstractProductRepository abstractProductRepository;
    @Autowired
    private ClientRepository clientRepository;
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam long clientId, @RequestParam Long productId,
                                            @RequestParam int quantity) {
        if (clientRepository.existsById(clientId) && abstractProductRepository.existsById(productId)) {
            Client client = clientRepository.getById(clientId);
            AbstractProduct abstractProduct = abstractProductRepository.getById(productId);
            if (!cartService.addToCart(client, abstractProduct, quantity)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Товар закончился");
            }
            return ResponseEntity.ok("Товар успешно добавлен в корзину.");
        } else return ResponseEntity.notFound().build();

    }
    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Long clientId, @RequestParam Long productId) {
        if (clientRepository.existsById(clientId) && abstractProductRepository.existsById(productId)) {
            Client client = clientRepository.getById(clientId);
            AbstractProduct abstractProduct = abstractProductRepository.getById(productId);
            if (!cartService.removeFromCart(client, abstractProduct))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такого товара в корзине не существует");
            return ResponseEntity.ok("Товар успешно удален из корзины.");
        } else return ResponseEntity.notFound().build();
    }
    @PostMapping("/update")
    public ResponseEntity<String> updateCartItemQuantity(@RequestParam Long clientId, @RequestParam Long productId,
                                                         @RequestParam int newQuantity) {
        if (clientRepository.existsById(clientId) && abstractProductRepository.existsById(productId)) {
            Client client = clientRepository.getById(clientId);
            AbstractProduct abstractProduct = abstractProductRepository.getById(productId);
            if (!cartService.updateCartItemQuantity(client, abstractProduct, newQuantity)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Товар закончился");
            }
            return ResponseEntity.ok("Количество товара в корзине обновлено.");
        } else return ResponseEntity.notFound().build();
    }
    @GetMapping("/view")
    public ResponseEntity<List<CartItemDTO>> viewCart(@RequestParam Long clientId) {
        if (clientRepository.existsById(clientId)) {
            Client client = clientRepository.getById(clientId);
            List<CartItem> cartItems = cartService.getCartItems(client);
            List<CartItemDTO> cartItemDTOs = cartItems.stream()
                    .map(cartItem -> new CartItemDTO(cartItem.getId(), cartItem.getAbstractProduct().getId(),
                            cartItem.getAbstractProduct().getName(), cartItem.getAbstractProduct().getPrice(),
                            cartItem.getQuantity(), client.getId(), client.getName())).toList();
            return ResponseEntity.ok(cartItemDTOs);
        } else return ResponseEntity.notFound().build();
    }
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestParam Long clientId) {
        if (clientRepository.existsById(clientId)) {
            Client client = clientRepository.getById(clientId);
            if (cartService.checkout(client) != null)
                return ResponseEntity.status(HttpStatus.CONFLICT).body(cartService.checkout(client));
            else {
                return ResponseEntity.ok("Заказ успешно оформлен.");
            }
        } else return ResponseEntity.notFound().build();
    }
}
