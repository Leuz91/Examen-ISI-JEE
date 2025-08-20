package isi.eshop.web;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1")
public class ApiIndexController {
  @GetMapping
  public RepresentationModel<?> index(){
    var model = new RepresentationModel<>();
    model.add(linkTo(methodOn(ApiIndexController.class).index()).withSelfRel());
    model.add(linkTo(methodOn(CategoryController.class).all()).withRel("categories"));
    model.add(linkTo(methodOn(ProductController.class).all()).withRel("products"));
    model.add(linkTo(methodOn(UserController.class).all()).withRel("users"));
    model.add(linkTo(methodOn(OrderController.class).all()).withRel("orders"));
    return model;
  }
}
