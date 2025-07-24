package ru.otus.order.service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.order.service.model.entity.Cart;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
}
