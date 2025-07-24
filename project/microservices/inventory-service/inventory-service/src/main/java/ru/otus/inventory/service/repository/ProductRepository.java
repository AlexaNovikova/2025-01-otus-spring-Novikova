package ru.otus.inventory.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.inventory.service.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
