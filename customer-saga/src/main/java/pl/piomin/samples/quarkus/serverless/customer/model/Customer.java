package pl.piomin.samples.quarkus.serverless.customer.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {

    @Id
    private Long id;
    private String name;
    private int amountAvailable;
    private int amountReserved;
}
