package com.projet.auth.controller;

import com.projet.auth.model.User;
import com.projet.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:8090")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Create: Ajouter un nouvel utilisateur
    @PostMapping
    public User createUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        return userService.createUser(username, email, password);
    }

    // Read: Obtenir un utilisateur par son ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    // Read: Obtenir tous les utilisateurs
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Update: Modifier les informations d'un utilisateur
    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable String id,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam(required = false) String password) {
        return userService.updateUser(id, username, email, password);
    }

    // Delete: Supprimer un utilisateur par son ID
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "User with ID " + id + " deleted successfully";
    }
    // Sign up: Inscrire un nouvel utilisateur
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String email = userData.get("email");
        String password = userData.get("password");

        try {
            User newUser = userService.signUp(username, email, password);
            return ResponseEntity.ok(Map.of("success", true, "message", "User registered successfully", "user", newUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        boolean isAuthenticated = userService.loginUser(email, password);
        if (isAuthenticated) {
            User user = userService.getUserByEmail(email); // Ajouter une méthode pour récupérer l'utilisateur par email
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login successful",
                    "userId", user.getId() // Retourner l'ID utilisateur
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Invalid email or password"
            ));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            userService.sendVerificationCode(email);
            return ResponseEntity.ok("Un code de vérification a été envoyé à l'adresse email fournie.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Endpoint pour vérifier le code et réinitialiser le mot de passe
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String verificationCode = request.get("code");
        String newPassword = request.get("newPassword");

        try {
            userService.resetPassword(email, verificationCode, newPassword);
            return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


}
