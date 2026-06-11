package com.project;

import com.vaadin.flow.theme.aura.Aura;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.models.Usuario;
import com.project.repositories.UsuarioRepository;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;


@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@StyleSheet("styles.css") // Your custom styles
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    /*
    @Bean
    CommandLineRunner testPassword(PasswordEncoder encoder) {
        return args -> {
            System.out.println("ADMIN HASH:");
            System.out.println(encoder.encode("admin"));

            System.out.println("USER HASH:");
            System.out.println(encoder.encode("user"));
        };
    }
    */
    
    @Bean
    CommandLineRunner init(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByNombreUsuario("admin")) {
                Usuario u = new Usuario();
                u.setNombreUsuario("admin");
                u.setPassword(encoder.encode("admin"));
                repo.save(u);
            }
        };
    }
}
