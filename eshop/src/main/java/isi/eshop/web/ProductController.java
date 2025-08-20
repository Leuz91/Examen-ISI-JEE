package isi.eshop.web;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;
import isi.eshop.domain.*;
import isi.eshop.repository.*;
import isi.eshop.web.dto.CreateProductRequest;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductRepository products;
  private final CategoryRepository categories;

  @GetMapping
  public CollectionModel<EntityModel<Product>> all(){
    var models = products.findAll().stream().map(this::toModel).toList();
    return CollectionModel.of(models, linkTo(methodOn(ProductController.class).all()).withSelfRel());
  }

  @GetMapping("/{id}")
  public EntityModel<Product> byId(@PathVariable Long id){
    return products.findById(id).map(this::toModel).orElseThrow();
  }

  @GetMapping("/by-category/{categoryId}")
  public CollectionModel<EntityModel<Product>> byCategory(@PathVariable Long categoryId){
    var cat = categories.findById(categoryId).orElseThrow();
    var models = products.findAll().stream().filter(p -> p.getCategory().getId().equals(cat.getId())).map(this::toModel).toList();
    return CollectionModel.of(models,
      linkTo(methodOn(ProductController.class).byCategory(categoryId)).withSelfRel(),
      linkTo(methodOn(CategoryController.class).byId(categoryId)).withRel("category")
    );
  }

  @PostMapping
  public EntityModel<Product> create(@RequestBody @Valid CreateProductRequest req){
    var cat = categories.findById(req.categoryId()).orElseThrow();
    var saved = products.save(Product.builder().name(req.name()).price(req.price()).category(cat).build());
    return toModel(saved);
  }

  private EntityModel<Product> toModel(Product p){
    return EntityModel.of(p,
      linkTo(methodOn(ProductController.class).byId(p.getId())).withSelfRel(),
      linkTo(methodOn(ProductController.class).all()).withRel("collection"),
      linkTo(methodOn(CategoryController.class).byId(p.getCategory().getId())).withRel("category")
    );
  }
}
