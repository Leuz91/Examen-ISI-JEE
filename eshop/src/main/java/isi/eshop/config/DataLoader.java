package isi.eshop.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import isi.eshop.domain.*;
import isi.eshop.repository.*;

@Configuration
public class DataLoader {
  @Bean CommandLineRunner init(isi.eshop.repository.CategoryRepository catRepo, isi.eshop.repository.ProductRepository prodRepo, isi.eshop.repository.UserRepository userRepo){
    return args -> {
      var cat1 = catRepo.save(isi.eshop.domain.Category.builder().name("Electronics").build());
      var cat2 = catRepo.save(isi.eshop.domain.Category.builder().name("Books").build());

      prodRepo.save(isi.eshop.domain.Product.builder().name("Laptop Pro").price(1499.0).category(cat1).build());
      prodRepo.save(isi.eshop.domain.Product.builder().name("USB-C Hub").price(39.9).category(cat1).build());
      prodRepo.save(isi.eshop.domain.Product.builder().name("Clean Architecture").price(29.0).category(cat2).build());

      userRepo.save(isi.eshop.domain.User.builder().firstName("Awa").lastName("Diop").email("awa@eshop.sn").build());
      userRepo.save(isi.eshop.domain.User.builder().firstName("Moussa").lastName("Ndiaye").email("moussa@eshop.sn").build());
    };
  }
}
