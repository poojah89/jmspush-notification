package com.aut.jmspushnotification.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class Notification {

    @JsonAlias("ID")
    private BigDecimal id ;

    @JsonAlias("TRADER_ID")
    private String trader_id;

    @JsonAlias("TITLE")
    private String title;

    @JsonAlias("MESSAGE")
    private String message;
           
    @JsonAlias("BROADCAST_TIME")
    private String broadcast_time;
}