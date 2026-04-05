package com.crewmeister.cmcodingchallenge.currency.entity;

import com.crewmeister.cmcodingchallenge.currency.entity.key.ConversionRateKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * The ConversionRate entity that will contain the following columns
 * - CURRENCY_ID - the foreign key to the CURRENCY for this given conversion rate
 * - DATE - the day for the given conversion rate for this currency
 * - RATE - the rate (converted from Euros) for this currency on this date
 */
@Entity
@IdClass(ConversionRateKey.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversionRate {

    @ManyToOne
    @Id
    private Currency currency;

    @Temporal(TemporalType.DATE)
    @Id
    private Date date;

    private Double rate;

}
