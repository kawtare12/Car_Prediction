package com.projet.auth.service;


import com.projet.auth.model.User;
import com.projet.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private String lastVerificationCode = null; // Stocke le dernier code de vérification généré
    private String verificationEmail = null;    // Email de l'utilisateur pour lequel le code a été généré

    // Méthode pour réinitialiser le mot de passe
    public void resetPassword(String email, String verificationCode, String newPassword) {
        // Vérifier si l'email existe
        if (!userExists(email)) {
            throw new RuntimeException("Aucun utilisateur trouvé avec cet email.");
        }

        // Vérifier si le code de vérification est correct
        if (!verificationCode.equals(lastVerificationCode)) {
            throw new RuntimeException("Code de vérification incorrect.");
        }

        // Récupérer l'utilisateur
        User user = userRepository.findByEmail(email);

        // Réinitialiser le mot de passe de l'utilisateur
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    // Générer et envoyer un code de vérification par email
    public void sendVerificationCode(String email) {
        if (!userExists(email)) {
            throw new RuntimeException("Aucun utilisateur trouvé avec cet email.");
        }

        // Générer un code de vérification
        String verificationCode = emailService.generateVerificationCode();
        lastVerificationCode = verificationCode;
        verificationEmail = email;

        // Envoyer le code par email
        emailService.sendVerificationCode(email, verificationCode);
    }



    public User createUser(String username, String email, String password) {
        if (userExists(email)) {
            throw new RuntimeException("User already exists with email: " + email);
        }
        User user = new User(username, email, password);
        return userRepository.save(user);
    }

    // Read: Récupérer un utilisateur par son ID
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Read: Récupérer tous les utilisateurs
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update: Modifier les informations d'un utilisateur
    public User updateUser(String id, String username, String email, String password) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(username);
            user.setEmail(email);
            if (password != null && !password.isEmpty()) {
                user.setPassword(password); // Le setter gère le hachage
            }
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with ID: " + id);
        }
    }

    // Delete: Supprimer un utilisateur par son ID
    public void deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with ID: " + id);
        }
    }
    // Sign up: Inscription d'un nouvel utilisateur
    public User signUp(String username, String email, String password) {
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Username cannot be null or empty");
        }
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email cannot be null or empty");
        }
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("Password cannot be null or empty");
        }
        if (userExists(email)) {
            throw new RuntimeException("User already exists with email: " + email);
        }

        // Création de l'utilisateur
        User newUser = new User(username, email, password);
        return userRepository.save(newUser);
    }

    // Vérifier si un utilisateur existe déjà par email
    public boolean userExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
    public boolean loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return true; // Connexion réussie
        }
        return false; // Échec de la connexion
    }

}
