package com.studentmanagement.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.studentmanagement.entity.User;
import com.studentmanagement.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap.admin.username:}") String adminUsername,
            @Value("${app.bootstrap.admin.password:}") String adminPassword,
            @Value("${app.bootstrap.user.username:}") String userUsername,
            @Value("${app.bootstrap.user.password:}") String userPassword) {

        return args -> {
            if (userRepository.count() != 0) {
                return;
            }

            boolean usernameConfigured = StringUtils.hasText(adminUsername);
            boolean passwordConfigured = StringUtils.hasText(adminPassword);
            boolean userUsernameConfigured = StringUtils.hasText(userUsername);
            boolean userPasswordConfigured = StringUtils.hasText(userPassword);

            if (usernameConfigured != passwordConfigured) {
                throw new IllegalStateException(
                        "Set both APP_BOOTSTRAP_ADMIN_USERNAME and APP_BOOTSTRAP_ADMIN_PASSWORD.");
            }
            if (userUsernameConfigured != userPasswordConfigured) {
                throw new IllegalStateException(
                        "Set both APP_BOOTSTRAP_USER_USERNAME and APP_BOOTSTRAP_USER_PASSWORD.");
            }

            if (!usernameConfigured) {
                throw new IllegalStateException(
                        "No users exist. Set APP_BOOTSTRAP_ADMIN_USERNAME and "
                                + "APP_BOOTSTRAP_ADMIN_PASSWORD for first startup.");
            }
            if (userUsernameConfigured && adminUsername.equalsIgnoreCase(userUsername)) {
                throw new IllegalStateException(
                        "Bootstrap admin and user usernames must be different.");
            }

            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole("ADMIN");
            admin.setEnabled(true);

            List<User> initialUsers = new ArrayList<>();
            initialUsers.add(admin);

            if (userUsernameConfigured) {
                User user = new User();
                user.setUsername(userUsername);
                user.setPassword(passwordEncoder.encode(userPassword));
                user.setRole("USER");
                user.setEnabled(true);
                initialUsers.add(user);
            }
            userRepository.saveAll(initialUsers);
        };
    }
}