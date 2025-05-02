package com.ordermanager.config;

import javax.ws.rs.ApplicationPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ordermanager.controller.ItemController;
import com.ordermanager.repository.ItemRepository;
import com.ordermanager.service.ItemService;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationPath("/api")     //Isso ativa os endpoints REST no caminho /api
public class AppConfig extends ResourceConfig {
    public AppConfig() {
        packages("com.ordermanager.controller");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));

        // REgistar suporte a JSON + Java Time (LocalDateTime, etc)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(objectMapper);

        // Isso é crucial para Docker
        register(provider);
        register(JacksonFeature.class);
        // Para melhor tratamento de JSON
        property(ServerProperties.MEDIA_TYPE_MAPPINGS,"json:application/json");
        property(ServerProperties.WADL_FEATURE_DISABLE, true);

    }
}

