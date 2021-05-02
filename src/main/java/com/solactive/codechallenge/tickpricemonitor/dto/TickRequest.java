package com.solactive.codechallenge.tickpricemonitor.dto;

import com.solactive.codechallenge.tickpricemonitor.validator.NotNullField;
import com.solactive.codechallenge.tickpricemonitor.validator.ValidTimeStamp;
import lombok.*;

import java.io.Serializable;

/**
 * @author Ramesh.Yaleru on 4/29/2021
 */
@Getter
@Setter
@Builder
@ToString
@NotNullField.List({
        @NotNullField(fieldName = "instrument", message = "instrument must not be null or empty"),
        @NotNullField(fieldName = "price", message = "price can not be null or empty."),
        @NotNullField(fieldName = "timeStamp", message = "timeStamp can not be null or empty."),

})
@NoArgsConstructor
@AllArgsConstructor
public class TickRequest implements Serializable {
    private String instrument;
    private Double price;
    private long timeStamp;
}
