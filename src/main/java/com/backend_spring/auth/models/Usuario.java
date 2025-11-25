package com.backend_spring.auth.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "id_sede", nullable = true)
    private Long idSede;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 100, nullable = false)
    private String apellido;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(length = 20, nullable = false, unique = true)
    private String telefono;

    @Column(length = 15, nullable = false, unique = true)
    private String dni;

    @Column(nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String estado = "activo";

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol tipo;

    @OneToMany(
            mappedBy = "usuario",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Token> tokens;
}
