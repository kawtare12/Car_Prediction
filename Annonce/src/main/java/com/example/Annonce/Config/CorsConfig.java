package com.example.Annonce.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Liste des origines spécifiques autorisées (ici l'API Gateway)
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8090")  // Remplacer par l'URL de votre API Gateway
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes autorisées
                .allowedHeaders("*") // Autorise toutes les en-têtes
                .allowCredentials(true);  // Autorise l'envoi d'informations d'identification (cookies, etc.)
    }
}