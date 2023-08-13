package com.syrisa.tr.productsservice.core.data;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
@Entity
@Table(name = "products")
@Data
public class ProductEntity implements Serializable {

    private static final long serialVersionUID = 123456789L;

    @Id
    @Column(unique = true)
    private String productId;
   // @Column(unique = true, nullable = false)
    @Column(unique = false, nullable = false)
    //NOT NULL UNIQUE
    private String title;
    private Integer quantity;
    private BigDecimal price;

}
