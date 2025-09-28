package com.example.softwarebackend.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig
{
    @Value("${GOOGLE.API.KEY}")
    private String apiKey;

    @Bean
    public Client getClient()
    {
        return new Client.Builder()
                .apiKey(apiKey)
                .build();
    }
}
