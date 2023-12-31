package com.syrisa.tr.productsservice.core.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_lookup")
public class ProductLookupEntity implements Serializable {

    private static final long serialVersionUID = 123456789L;
    @Id
    private String productId;
    @Column(nullable = false,unique = true)
    private String title;
}
