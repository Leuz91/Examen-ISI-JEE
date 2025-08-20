package isi.eshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import isi.eshop.domain.CustomerOrder;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {}
