package com.solactive.codechallenge.tickpricemonitor.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticResponse implements Serializable {
    private double avg;
    private double max;
    private double min;
    private long count =0;

    @JsonIgnore
    private long calculatedTimeStamp;
}
