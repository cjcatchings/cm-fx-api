package com.crewmeister.cmcodingchallenge.currency.entity;

import com.crewmeister.cmcodingchallenge.currency.entity.key.ConversionRateKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
