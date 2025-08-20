package isi.eshop.web;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;
import isi.eshop.domain.User;
import isi.eshop.repository.UserRepository;
import isi.eshop.web.dto.CreateUserRequest;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserRepository users;

  @GetMapping
  public CollectionModel<EntityModel<User>> all(){
    var models = users.findAll().stream().map(this::toModel).toList();
    return CollectionModel.of(models, linkTo(methodOn(UserController.class).all()).withSelfRel());
  }

  @GetMapping("/{id}")
  public EntityModel<User> byId(@PathVariable Long id){
    return users.findById(id).map(this::toModel).orElseThrow();
  }

  @PostMapping
  public EntityModel<User> create(@RequestBody @Valid CreateUserRequest req){
    var saved = users.save(User.builder().firstName(req.firstName()).lastName(req.lastName()).email(req.email()).build());
    return toModel(saved);
  }

  private EntityModel<User> toModel(User u){
    return EntityModel.of(u,
      linkTo(methodOn(UserController.class).byId(u.getId())).withSelfRel(),
      linkTo(methodOn(UserController.class).all()).withRel("collection"),
      linkTo(methodOn(OrderController.class).byUser(u.getId())).withRel("orders")
    );
  }
}
