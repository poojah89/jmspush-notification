package com.aut.jmspushnotification.utils;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.aut.jmspushnotification.model.Notification;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class JSONConverter {

    private ObjectMapper mapper;

    public JSONConverter() {
        this.mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule=new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        this.mapper.registerModule(new ParameterNamesModule());
        this.mapper.registerModule(new Jdk8Module());
        this.mapper.registerModule(javaTimeModule);
        this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public Map<String, Object> convertResponse(String req)throws IOException {
        return mapper.readValue(req, HashMap.class);
    }

    public Notification convertData(String req)throws IOException{
        return mapper.readValue(req, Notification.class);
    }

}
