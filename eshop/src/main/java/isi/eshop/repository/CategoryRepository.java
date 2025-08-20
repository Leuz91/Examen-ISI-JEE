package isi.eshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import isi.eshop.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
