package com.example.Annonce.service;

import com.example.Annonce.model.Annonce;
import com.example.Annonce.repository.AnnonceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnonceService {

    @Autowired
    private AnnonceRepository annonceRepository;

    public Annonce saveAnnonce(Annonce annonce) {
        return annonceRepository.save(annonce);
    }

    public List<Annonce> getAllAnnonces() {
        return annonceRepository.findAll();
    }

    public List<Annonce> getAnnoncesByUserId(String userId) {
        return annonceRepository.findByUserId(userId);
    }

    public Optional<Annonce> getAnnonceById(String id) {
        return annonceRepository.findById(id);
    }

    public void deleteAnnonce(String id) {
        annonceRepository.deleteById(id);
    }

    // Ajouter la méthode updateAnnonce dans le service
    public Annonce updateAnnonce(String id, Annonce annonceDetails) throws Exception {
        Optional<Annonce> existingAnnonce = annonceRepository.findById(id);
        if (existingAnnonce.isPresent()) {
            Annonce annonce = existingAnnonce.get();
            annonce.setDescription(annonceDetails.getDescription());
            annonce.setPrice(annonceDetails.getPrice());
            annonce.setMileage(annonceDetails.getMileage());
            annonce.setSeats(annonceDetails.getSeats());
            annonce.setDoors(annonceDetails.getDoors());
            annonce.setHorsepower(annonceDetails.getHorsepower());
            annonce.setFuelType(annonceDetails.getFuelType());
            annonce.setCarColor(annonceDetails.getCarColor());
            annonce.setCarBrand(annonceDetails.getCarBrand());
            annonce.setImage(annonceDetails.getImage()); // Si vous souhaitez mettre à jour l'image
            return annonceRepository.save(annonce);
        } else {
            throw new Exception("Annonce non trouvée pour l'ID: " + id);
        }
    }
}
