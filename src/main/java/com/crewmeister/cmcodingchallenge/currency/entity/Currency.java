package com.crewmeister.cmcodingchallenge.currency.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes = {
        @Index(
            name = "ix_currency_code",
            columnList = "currencyCode",
            unique = true
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String currencyCode;
    private String currencyName;
}
