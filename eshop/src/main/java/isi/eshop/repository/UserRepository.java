package isi.eshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import isi.eshop.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {}
