package com.example.Annonce.repository;
import com.example.Annonce.model.Annonce;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnnonceRepository extends MongoRepository<Annonce, String> {
    // Vous pouvez ajouter des méthodes personnalisées si nécessaire
    List<Annonce> findByUserId(String userId);

}