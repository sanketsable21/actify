package com.actify.config;

import com.actify.entity.Role;
import com.actify.entity.User;
import com.actify.repository.RoleRepository;
import com.actify.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedData(RoleRepository roleRepository,
                                       UserRepository userRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            // ===== Seed Roles (only if not present) =====
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

            Role managerRole = roleRepository.findByName("MANAGER")
                    .orElseGet(() -> roleRepository.save(new Role("MANAGER")));

            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(new Role("USER")));

            // ===== Seed Users (only if not present) =====
            if (!userRepository.existsByEmail("admin@actify.com")) {
                User admin = new User();
                admin.setName("Admin User");
                admin.setEmail("admin@actify.com");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setRoles(Set.of(adminRole, userRole));
                userRepository.save(admin);
                System.out.println(">>> Seeded ADMIN user: admin@actify.com / Admin@123");
            }

            if (!userRepository.existsByEmail("manager@actify.com")) {
                User manager = new User();
                manager.setName("Manager User");
                manager.setEmail("manager@actify.com");
                manager.setPassword(passwordEncoder.encode("Manager@123"));
                manager.setRoles(Set.of(managerRole, userRole));
                userRepository.save(manager);
                System.out.println(">>> Seeded MANAGER user: manager@actify.com / Manager@123");
            }

            if (!userRepository.existsByEmail("user@actify.com")) {
                User user = new User();
                user.setName("Regular User");
                user.setEmail("user@actify.com");
                user.setPassword(passwordEncoder.encode("User@123"));
                user.setRoles(Set.of(userRole));
                userRepository.save(user);
                System.out.println(">>> Seeded USER user: user@actify.com / User@123");
            }

            System.out.println(">>> Data initialization complete.");
        };
    }
}