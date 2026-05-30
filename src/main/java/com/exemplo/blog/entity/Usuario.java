package com.exemplo.blog.entity; 
  
import jakarta.persistence.*; 
import lombok.*; 
import java.time.LocalDateTime; 
import java.util.HashSet; 
import java.util.Set; 
  
@Entity 
@Table(name = "usuarios") 
@Getter @Setter 
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Usuario { 
  
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
  
    @Column(nullable = false, length = 100) 
    private String nome; 
  
    @Column(nullable = false, unique = true, length = 150) 
    private String email; 
  
    @Column(nullable = false) 
    private String senha; 
  
    @Column(length = 500) 
    private String bio; 
  
    @Column(name = "avatar_url") 
    private String avatarUrl; 
  
    @Builder.Default
    @Enumerated(EnumType.STRING) 
    @Column(nullable = false) 
    private Role role = Role.LEITOR; 
  
    @Column(name = "criado_em", nullable = false, updatable = false) 
    private LocalDateTime criadoEm; 
  
    @Column(name = "atualizado_em") 
    private LocalDateTime atualizadoEm; 
  
    @Builder.Default
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true) 
    @ToString.Exclude 
    private Set<Post> posts = new HashSet<>(); 
  
    @PrePersist 
    protected void onCreate() { 
        criadoEm = LocalDateTime.now(); 
        atualizadoEm = LocalDateTime.now(); 
    } 
  
    @PreUpdate 
    protected void onUpdate() { 
        atualizadoEm = LocalDateTime.now(); 
    } 
  
    public enum Role { 
        LEITOR, AUTOR, ADMIN 
    } 
} 