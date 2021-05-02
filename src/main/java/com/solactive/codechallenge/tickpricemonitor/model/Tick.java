package com.solactive.codechallenge.tickpricemonitor.model;

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
public class Tick implements Serializable {

    private double price;
    private long timestamp;
}
