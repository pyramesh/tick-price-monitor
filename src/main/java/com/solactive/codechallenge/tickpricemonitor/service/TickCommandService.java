package com.solactive.codechallenge.tickpricemonitor.service;

import com.solactive.codechallenge.tickpricemonitor.dto.TickRequest;
import com.solactive.codechallenge.tickpricemonitor.dto.TickResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
public interface TickCommandService {

   TickResponse saveTick(TickRequest tickRequest);
}
