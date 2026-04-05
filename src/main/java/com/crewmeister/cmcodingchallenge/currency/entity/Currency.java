package com.crewmeister.cmcodingchallenge.currency.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Currency entity that will contain the following columns
 *  - ID - The generated unique ID for the currency
 *  - CURRENCY_CODE - The 3-letter code for the currency
 *  - CURRENCY_NAME - A descriptive name for the currency
 */
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
