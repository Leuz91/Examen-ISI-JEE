package isi.eshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import isi.eshop.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {}
