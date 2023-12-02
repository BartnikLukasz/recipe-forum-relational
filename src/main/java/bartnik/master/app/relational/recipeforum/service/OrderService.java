package bartnik.master.app.relational.recipeforum.service;

import bartnik.master.app.relational.recipeforum.dto.request.OrderProductReportRequest;
import bartnik.master.app.relational.recipeforum.dto.request.OrderProductsRequest;
import bartnik.master.app.relational.recipeforum.dto.request.OrderReportRequest;
import bartnik.master.app.relational.recipeforum.model.LineItem;
import bartnik.master.app.relational.recipeforum.model.Order;
import bartnik.master.app.relational.recipeforum.repository.*;
import bartnik.master.app.relational.recipeforum.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderRepositoryCrud orderRepositoryCrud;
    private final LineItemRepository lineItemRepository;
    private final ProductRepository productRepository;
    private final CustomUserRepository userRepository;

    @Transactional
    public void orderProducts(OrderProductsRequest request) {
        var currentUser = UserUtil.getCurrentUser();
        var user = userRepository.getByUsername(currentUser.getUsername());

        List<LineItem> items = request.getLineItems().stream()
                .map(lineItem -> {
            var product = productRepository.findById(lineItem.getProductId()).orElseThrow();
            if (product.getAvailability() < lineItem.getQuantity()) {
                throw new ResourceNotFoundException();
            }
            product.setAvailability(product.getAvailability() - lineItem.getQuantity());
            productRepository.save(product);
            return LineItem.builder()
                    .product(product)
                    .quantity(lineItem.getQuantity())
                    .build();
        })
                .collect(Collectors.toList());

        var order = Order.builder()
                .items(items)
                .user(user)
                .value(items.stream().map(
                        item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .orderDate(LocalDateTime.now())
                .build();

        items.forEach(item -> item.setOrder(order));
        var savedOrder = orderRepository.save(order);
        user.getOrders().add(savedOrder);
        userRepository.save(user);
        lineItemRepository.saveAll(items);
        orderRepository.save(savedOrder);
    }

    public List<Order> generateReport(OrderReportRequest request) {
        return orderRepositoryCrud.findAll(request);
    }

    public List<LineItem> generateProductReport(OrderProductReportRequest request) {
        return orderRepositoryCrud.findAll(request);
    }
}
