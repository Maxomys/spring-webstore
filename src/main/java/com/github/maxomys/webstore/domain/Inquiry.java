package com.github.maxomys.webstore.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Email
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @NotNull
    @Lob
    private String messageBody;

    @ManyToOne
    private Product product;

}
