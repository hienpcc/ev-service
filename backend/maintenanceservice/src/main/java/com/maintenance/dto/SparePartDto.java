package com.maintenance.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SparePartDto {
    private Long partId;
    private String name;
    private String description;
    private Integer quantityInStock;
    private Integer warnStockLevel;
    private BigDecimal price;
}
