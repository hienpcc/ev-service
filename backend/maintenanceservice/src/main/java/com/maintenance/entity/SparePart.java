package com.maintenance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "spare_part")
@Getter
@Setter
public class SparePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_id")
    private Long partId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity_in_stock")
    private Integer quantityInStock;

    @Column(name = "warn_stock_level")
    private Integer warnStockLevel; // Mức cảnh báo tồn kho tối thiểu

    @Column(name = "price")
    private BigDecimal price;
}
