package isi.eshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import isi.eshop.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
