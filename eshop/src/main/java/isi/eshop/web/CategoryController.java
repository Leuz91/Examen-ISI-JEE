package isi.eshop.web;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.*;
import org.springframework.web.bind.annotation.*;
import isi.eshop.domain.Category;
import isi.eshop.repository.CategoryRepository;
import isi.eshop.web.dto.CreateCategoryRequest;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryRepository repo;

  @GetMapping
  public CollectionModel<EntityModel<Category>> all(){
    var models = repo.findAll().stream().map(this::toModel).toList();
    return CollectionModel.of(models, linkTo(methodOn(CategoryController.class).all()).withSelfRel());
  }

  @GetMapping("/{id}")
  public EntityModel<Category> byId(@PathVariable Long id){
    var cat = repo.findById(id).orElseThrow();
    return toModel(cat);
  }

  @PostMapping
  public EntityModel<Category> create(@RequestBody @Valid CreateCategoryRequest req){
    var saved = repo.save(Category.builder().name(req.name()).build());
    return toModel(saved);
  }

  private EntityModel<Category> toModel(Category c){
    return EntityModel.of(c,
      linkTo(methodOn(CategoryController.class).byId(c.getId())).withSelfRel(),
      linkTo(methodOn(CategoryController.class).all()).withRel("collection"),
      linkTo(methodOn(ProductController.class).byCategory(c.getId())).withRel("products")
    );
  }
}
