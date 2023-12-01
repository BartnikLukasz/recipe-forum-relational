package bartnik.master.app.relational.recipeforum.repository;

import bartnik.master.app.relational.recipeforum.dto.request.OrderProductReportRequest;
import bartnik.master.app.relational.recipeforum.dto.request.OrderReportRequest;
import bartnik.master.app.relational.recipeforum.model.LineItem;
import bartnik.master.app.relational.recipeforum.model.Order;
import bartnik.master.app.relational.recipeforum.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
@Transactional
public class OrderRepositoryCrud {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<Order> findAll(OrderReportRequest request) {
        Query query = new Query();
        query.addCriteria(where("orderDate").gt(request.getFrom().atStartOfDay()).lt(request.getTo().atStartOfDay()));
        if (!request.getUserIds().isEmpty()) {
            query.addCriteria(where("user").in(request.getUserIds()));
        }
        if (!request.getExcludedUserIds().isEmpty()) {
            query.addCriteria(where("user").not().in(request.getExcludedUserIds()));
        }
        return mongoTemplate.find(query, Order.class);
    }

    public List<LineItem> findAll(OrderProductReportRequest request) {
        Query query = new Query();
        Query orderQuery = new Query();
        Query productQuery = new Query();
        orderQuery.addCriteria(where("orderDate").gt(request.getFrom().atStartOfDay()).lt(request.getTo().atStartOfDay()));
        if (!request.getUserIds().isEmpty()) {
            orderQuery.addCriteria(where("user").in(request.getUserIds()));
        }
        if (!request.getExcludedUserIds().isEmpty()) {
            orderQuery.addCriteria(where("user").not().in(request.getExcludedUserIds()));
        }
        if (!request.getProductIds().isEmpty()) {
            productQuery.addCriteria(where("_id").in(request.getProductIds()));
        }
        if (!request.getExcludedProductIds().isEmpty()) {
            productQuery.addCriteria(where("_id").not().in(request.getExcludedProductIds()));
        }
        if (!request.getProductCategoriesIds().isEmpty()) {
            productQuery.addCriteria(where("category").in(request.getProductCategoriesIds()));
        }
        if (!request.getExcludedProductCategoriesIds().isEmpty()) {
            productQuery.addCriteria(where("category").not().in(request.getExcludedProductCategoriesIds()));
        }
        orderQuery.fields().include("_id");
        productQuery.fields().include("_id");
        var orders = mongoTemplate.find(query, Order.class).stream().map(Order::getId).toList();
        var products = mongoTemplate.find(query, Product.class).stream().map(Product::getId).toList();
        query.addCriteria(where("order").in(orders));
        query.addCriteria(where("product").in(products));
        return mongoTemplate.find(query, LineItem.class);
    }
}
