package com.solactive.codechallenge.tickpricemonitor.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

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
public class TickResponse implements Serializable {
    private HttpStatus statusCode;
    private String message;
}
