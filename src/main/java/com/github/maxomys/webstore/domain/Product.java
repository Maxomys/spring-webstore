package com.github.maxomys.webstore.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer price;
    private LocalDateTime createdAt;
    private Integer amountInStock;

    @ElementCollection
    private Set<String> uniqueAddresses = new HashSet<>();

    @ManyToOne
    private User user;

    @Lob
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<Image> images = new ArrayList<>();

    private String phoneNumber;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Set<Inquiry> inquiries = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<Transaction> transactions = new HashSet<>();

    public void decreaseStockBy(int amount) {
        amountInStock -= amount;
    }

}
