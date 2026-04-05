package com.crewmeister.cmcodingchallenge.currency.entity.key;

import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Embeddable
public class ConversionRateKey implements Serializable {

    @ManyToOne
    private Currency currency;

    @Temporal(TemporalType.DATE)
    private Date date;

}
