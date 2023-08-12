package com.syrisa.tr.productsservice.core.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<ProductEntity, String> {

    ProductEntity findByProductId(String productId);
    //@Query("select p from ProductEntity p where p.productId = ?1 or p.title = ?2")
    ProductEntity findByProductIdOrTitle(String productId, String title);

}
