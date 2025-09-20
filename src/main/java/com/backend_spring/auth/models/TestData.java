package com.backend_spring.auth.models;

import com.backend_spring.auth.config.SecurityConfig;
import com.backend_spring.auth.repositories.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TestData {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void init() {
        /*Usuario admin = new Usuario();
        admin.setNombre("Benjamin");
        admin.setApellido("Sullca");
        admin.setEmail("benjamin@example.com");
        admin.setTelefono("974666807");
        admin.setDni("76028393");
        admin.setPassword(bCryptPasswordEncoder.encode("123456"));
        admin.setTipo(Rol.ADMINISTRADOR);
        usuarioRepository.save(admin);*/
    }

}