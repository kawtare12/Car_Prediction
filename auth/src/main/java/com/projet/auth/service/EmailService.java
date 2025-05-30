package com.projet.auth.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Fonction pour envoyer un email avec le code de vérification
    public void sendVerificationCode(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Code de vérification");
        message.setText("Votre code de vérification est : " + verificationCode);

        mailSender.send(message);
    }

    // Fonction pour générer un code de vérification aléatoire
    public String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999); // Génère un nombre entre 0 et 999999
        return String.format("%06d", code); // Retourne le code avec 6 chiffres
    }
}
