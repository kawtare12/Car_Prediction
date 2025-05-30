package com.example.Annonce.controller;

import com.example.Annonce.model.Annonce;
import com.example.Annonce.service.AnnonceService;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/annonces")
public class AnnonceController {

    @Autowired
    private AnnonceService annonceService;

    @PostMapping("/create")
    public Annonce createAnnonce(
            @RequestParam String mileage,
            @RequestParam String seats,
            @RequestParam String doors,
            @RequestParam String horsepower,
            @RequestParam String description,
            @RequestParam String fuelType,
            @RequestParam String carColor,
            @RequestParam String carBrand,
            @RequestParam String userId,
            @RequestParam String price,
            @RequestParam("image") MultipartFile image) throws IOException {

        byte[] imageBytes = saveImage(image);

        Annonce annonce = new Annonce();
        annonce.setMileage(mileage);
        annonce.setSeats(seats);
        annonce.setDoors(doors);
        annonce.setHorsepower(horsepower);
        annonce.setFuelType(fuelType);
        annonce.setDescription(description);
        annonce.setCarColor(carColor);
        annonce.setCarBrand(carBrand);
        annonce.setImage(imageBytes);
        annonce.setUserId(userId);
        annonce.setPrice(price);

        return annonceService.saveAnnonce(annonce);
    }

    private byte[] saveImage(MultipartFile image) throws IOException {
        return image.getBytes();
    }

    @GetMapping("/")
    public List<Annonce> getAllAnnonces() {
        return annonceService.getAllAnnonces();
    }

    @GetMapping("/user/{userId}")
    public List<Annonce> getAnnoncesByUserId(@PathVariable String userId) {
        return annonceService.getAnnoncesByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteAnnonce(@PathVariable String id) {
        Annonce annonce = annonceService.getAnnonceById(id).orElse(null);
        if (annonce != null) {
            annonceService.deleteAnnonce(id);
        } else {
            throw new NotFoundException("Annonce non trouvée");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Annonce> updateAnnonce(@PathVariable String id, @RequestBody Annonce annonceDetails) {
        try {
            Annonce updatedAnnonce = annonceService.updateAnnonce(id, annonceDetails);
            return ResponseEntity.ok(updatedAnnonce);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
