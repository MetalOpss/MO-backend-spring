package com.backend_spring.auth.repositories;

import com.backend_spring.auth.models.Rol;
import com.backend_spring.auth.models.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository; // Inyectamos las dependencias para poder testear.

    @Test
    void findByemail_UsuarioExistente_RetornaUsuario() {
        // Construimos al objeto Usuario

        Usuario usuarioTest = new Usuario(
                "Benja",
                "Sullca",
                "benja@example.com",
                "+51974666807",
                "76028393",
                "bloodchain14",
                Rol.OPERARIO
        );

        // Guardando el usuario temp
        Usuario guardado = usuarioRepository.save(usuarioTest);

        // Busqueda como opcional
        Optional<Usuario> encontradoOptional = usuarioRepository.findByemail(guardado.getEmail());

        // Validadndo
        assertTrue(encontradoOptional.isPresent(), "El usuario no existe");

        // Obtener el usuario
        Usuario encontrado =  encontradoOptional.get();

        // Verificando si el email coincide con el esperado
        assertEquals("benja@example.com", encontrado.getEmail(), "El email ingresado no pertenece a ningun usuario");
    }
}

