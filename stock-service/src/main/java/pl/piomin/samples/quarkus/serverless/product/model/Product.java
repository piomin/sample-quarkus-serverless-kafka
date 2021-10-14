package pl.piomin.samples.quarkus.serverless.product.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    private Long id;
    private String name;
    private int availableItems;
    private int reservedItems;
}
