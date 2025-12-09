package com.library.users.config;

import com.library.users.dto.UserRegistrationDTO;
import com.library.users.repository.UserRepository;
import com.library.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandLineRunner para cargar datos iniciales de usuarios automáticamente
 * Solo se ejecuta si la propiedad 'users.load-initial-data' está en 'true'
 * y la base de datos está vacía
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "users.load-initial-data", havingValue = "true", matchIfMissing = false)
public class UserDataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        long userCount = userRepository.count();
        
        if (userCount == 0) {
            log.info("Base de datos vacía. Cargando usuarios precargados...");
            List<UserRegistrationDTO> initialUsers = getInitialUsers();
            var result = userService.loadUsersBulk(initialUsers);
            log.info("Carga inicial de usuarios completada: {}", result.getMessage());
        } else {
            log.info("Base de datos ya contiene {} usuarios. No se cargarán datos iniciales.", userCount);
        }
    }

    /**
     * Obtener la lista de usuarios precargados (2 usuarios)
     */
    private List<UserRegistrationDTO> getInitialUsers() {
        List<UserRegistrationDTO> users = new ArrayList<>();

        // Usuario Demo
        UserRegistrationDTO demoUser = new UserRegistrationDTO();
        demoUser.setName("Demo User");
        demoUser.setEmail("demo@duoc.cl");
        demoUser.setPhone("912345678");
        demoUser.setPassword("Demo123!");
        users.add(demoUser);

        // Usuario Admin
        UserRegistrationDTO adminUser = new UserRegistrationDTO();
        adminUser.setName("Admin User");
        adminUser.setEmail("admin123@gmail.com");
        adminUser.setPhone("000000000");
        adminUser.setPassword("admin12345678");
        users.add(adminUser);

        return users;
    }
}

