package isi.eshop.web;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import isi.eshop.domain.*;
import isi.eshop.repository.*;
import isi.eshop.web.dto.CreateOrderRequest;
import jakarta.validation.Valid;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
  private final CustomerOrderRepository orders;
  private final UserRepository users;
  private final ProductRepository products;
  private final OrderItemRepository items;

  @GetMapping
  public CollectionModel<EntityModel<CustomerOrder>> all(){
    var models = orders.findAll().stream().map(this::toModel).toList();
    return CollectionModel.of(models, linkTo(methodOn(OrderController.class).all()).withSelfRel());
  }

  @GetMapping("/{id}")
  public EntityModel<CustomerOrder> byId(@PathVariable Long id){
    return orders.findById(id).map(this::toModel).orElseThrow();
  }

  @GetMapping("/by-user/{userId}")
  public CollectionModel<EntityModel<CustomerOrder>> byUser(@PathVariable Long userId){
    var usr = users.findById(userId).orElseThrow();
    var models = orders.findAll().stream().filter(o -> o.getCustomer().getId().equals(usr.getId())).map(this::toModel).toList();
    return CollectionModel.of(models,
      linkTo(methodOn(OrderController.class).byUser(userId)).withSelfRel(),
      linkTo(methodOn(UserController.class).byId(userId)).withRel("user")
    );
  }

  @PostMapping
  @Transactional
  public EntityModel<CustomerOrder> create(@RequestBody @Valid CreateOrderRequest req){
    var user = users.findById(req.customerId()).orElseThrow();
    var order = orders.save(CustomerOrder.builder().customer(user).build());

    var list = new ArrayList<OrderItem>();
    for(var it : req.items()){
      var prod = products.findById(it.productId()).orElseThrow();
      var oi = items.save(OrderItem.builder().order(order).product(prod).quantity(it.quantity()).build());
      list.add(oi);
    }
    order.setItems(list);
    return toModel(order);
  }

  private EntityModel<CustomerOrder> toModel(CustomerOrder o){
    return EntityModel.of(o,
      linkTo(methodOn(OrderController.class).byId(o.getId())).withSelfRel(),
      linkTo(methodOn(OrderController.class).all()).withRel("collection"),
      linkTo(methodOn(UserController.class).byId(o.getCustomer().getId())).withRel("customer")
    );
  }
}
