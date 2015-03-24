package com.maurezen.nnpredictor.model;

import lombok.Value;

import java.util.Date;

/**
 * start,finish,duration,energy,id
 */
@Value
public class Session {
    Date start;
    Date finish;
    /**
     * Duration in seconds
     */
    long duration;
    double energy;
    String id;
}
