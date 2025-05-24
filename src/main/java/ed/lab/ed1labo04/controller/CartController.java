package ed.lab.ed1labo04.controller;

import ed.lab.ed1labo04.Entity.CartEntity;
import ed.lab.ed1labo04.model.CreateCartRequest;
import ed.lab.ed1labo04.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody CreateCartRequest request) {
        try {
            CartEntity cart = cartService.createCart(request);
            return ResponseEntity.status(201).body(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartEntity> getCart(@PathVariable Long id) {
        return cartService.getCartById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}