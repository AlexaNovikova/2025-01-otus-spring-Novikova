package ru.otus.inventory.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.inventory.service.entity.ReservedProduct;
import ru.otus.inventory.service.entity.ReservedProductId;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReservedProductRepository extends JpaRepository<ReservedProduct, ReservedProductId> {

    @Query("SELECT rp FROM ReservedProduct rp WHERE rp.id.productId in :productIds")
    List<ReservedProduct> findAllByProductIds(Collection<Long> productIds);

}
