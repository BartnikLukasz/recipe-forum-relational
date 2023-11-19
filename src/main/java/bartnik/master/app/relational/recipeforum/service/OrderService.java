package bartnik.master.app.relational.recipeforum.service;

import bartnik.master.app.relational.recipeforum.dto.request.OrderProductReportRequest;
import bartnik.master.app.relational.recipeforum.dto.request.OrderProductsRequest;
import bartnik.master.app.relational.recipeforum.dto.request.OrderReportRequest;
import bartnik.master.app.relational.recipeforum.model.LineItem;
import bartnik.master.app.relational.recipeforum.model.Order;
import bartnik.master.app.relational.recipeforum.repository.CustomUserRepository;
import bartnik.master.app.relational.recipeforum.repository.LineItemRepository;
import bartnik.master.app.relational.recipeforum.repository.OrderRepository;
import bartnik.master.app.relational.recipeforum.repository.ProductRepository;
import bartnik.master.app.relational.recipeforum.util.UserUtil;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static bartnik.master.app.relational.recipeforum.model.QLineItem.*;
import static bartnik.master.app.relational.recipeforum.model.QOrder.order;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final LineItemRepository lineItemRepository;
    private final ProductRepository productRepository;
    private final CustomUserRepository userRepository;

    @Transactional
    public void orderProducts(OrderProductsRequest request) {
        var currentUser = UserUtil.getCurrentUser();
        var user = userRepository.getByUsername(currentUser.getUsername());

        List<LineItem> items = request.getLineItems().stream()
                .map(lineItem -> {
            var product = productRepository.getReferenceById(lineItem.getProductId());
            if (product.getAvailability() < lineItem.getQuantity()) {
                throw new EntityNotFoundException();
            }
            product.setAvailability(product.getAvailability() - lineItem.getQuantity());
            productRepository.save(product);
            return LineItem.builder()
                    .product(product)
                    .quantity(lineItem.getQuantity())
                    .build();
        })
                .collect(Collectors.toList());
        items = lineItemRepository.saveAll(items);

        var order = Order.builder()
                .items(items)
                .user(user)
                .value(items.stream().map(
                        item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .orderDate(LocalDateTime.now())
                .build();

        items.forEach(item -> item.setOrder(order));

        orderRepository.save(order);
    }

    public List<Order> generateReport(OrderReportRequest request) {
        return IteratorUtils.toList(orderRepository.findAll(buildPredicate(request)).iterator());
    }

    public List<LineItem> generateProductReport(OrderProductReportRequest request) {
        return IteratorUtils.toList(lineItemRepository.findAll(buildProductPredicate(request)).iterator());
    }

    private BooleanBuilder buildPredicate(OrderReportRequest request) {
        var booleanBuilder = new BooleanBuilder();
            booleanBuilder.and(order.orderDate.after(request.getFrom().atStartOfDay())
                    .and((order.orderDate.before(request.getTo().plusDays(1).atStartOfDay()))));

        if (!request.getUserIds().isEmpty()) {
            booleanBuilder.and(order.user.id.in(request.getUserIds()));
        }
        if (!request.getExcludedUserIds().isEmpty()) {
            booleanBuilder.and(order.user.id.notIn(request.getExcludedUserIds()));
        }
        return booleanBuilder;
    }

    private BooleanBuilder buildProductPredicate(OrderProductReportRequest request) {
        var booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(lineItem.order.orderDate.after(request.getFrom().atStartOfDay())
                .and((lineItem.order.orderDate.before(request.getTo().plusDays(1).atStartOfDay()))));

        if (!request.getUserIds().isEmpty()) {
            booleanBuilder.and(lineItem.order.user.id.in(request.getUserIds()));
        }
        if (!request.getExcludedUserIds().isEmpty()) {
            booleanBuilder.and(lineItem.order.user.id.notIn(request.getExcludedUserIds()));
        }
        if (!request.getProductIds().isEmpty()) {
            booleanBuilder.and(lineItem.product.id.in(request.getProductIds()));
        }
        if (!request.getExcludedProductIds().isEmpty()) {
            booleanBuilder.and(lineItem.product.id.notIn(request.getExcludedProductIds()));
        }
        if (!request.getProductCategoriesIds().isEmpty()) {
            booleanBuilder.and(lineItem.product.productCategory.id.in(request.getProductCategoriesIds()));
        }
        if (!request.getExcludedProductCategoriesIds().isEmpty()) {
            booleanBuilder.and(lineItem.product.productCategory.id.notIn(request.getExcludedProductCategoriesIds()));
        }
        return booleanBuilder;
    }
}
