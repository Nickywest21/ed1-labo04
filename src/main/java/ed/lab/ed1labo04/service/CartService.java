package ed.lab.ed1labo04.service;

import ed.lab.ed1labo04.Entity.CartEntity;
import ed.lab.ed1labo04.Entity.CartItemEntity;
import ed.lab.ed1labo04.Entity.ProductEntity;
import ed.lab.ed1labo04.model.CartItemRequest;
import ed.lab.ed1labo04.model.CreateCartRequest;
import ed.lab.ed1labo04.repository.CartRepository;
import ed.lab.ed1labo04.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartEntity createCart(CreateCartRequest request) {
        List<CartItemEntity> items = new ArrayList<>();
        double totalPrice = 0;

        for (CartItemRequest itemReq : request.getCartItems()) {
            Optional<ProductEntity> productOpt = productRepository.findById(itemReq.getProductId());

            if (itemReq.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            if (productOpt.isEmpty()) {
                throw new IllegalArgumentException("Product not found");
            }

            ProductEntity product = productOpt.get();

            if (product.getQuantity() < itemReq.getQuantity()) {
                throw new IllegalArgumentException("Not enough inventory for product: " + product.getName());
            }

            // Resta inventario
            product.setQuantity(product.getQuantity() - itemReq.getQuantity());
            productRepository.save(product);

            CartItemEntity item = new CartItemEntity();
            item.setProductId(product.getId());
            item.setName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(itemReq.getQuantity());

            totalPrice += product.getPrice() * itemReq.getQuantity();
            items.add(item);
        }

        CartEntity cart = new CartEntity();
        cart.setCartItems(items);
        cart.setTotalPrice(totalPrice);
        return cartRepository.save(cart);
    }

    public Optional<CartEntity> getCartById(Long id) {
        return cartRepository.findById(id);
    }
}
