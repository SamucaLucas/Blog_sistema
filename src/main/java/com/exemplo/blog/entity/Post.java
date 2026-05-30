package com.exemplo.blog.entity; 
  
import jakarta.persistence.*; 
import lombok.*; 
import java.time.LocalDateTime; 
import java.util.HashSet; 
import java.util.Set; 
  
@Entity 
@Table(name = "posts") 
@Getter @Setter 
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Post { 
  
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
  
    @Column(nullable = false, length = 200) 
    private String titulo; 
  
    @Column(nullable = false, unique = true, length = 220) 
    private String slug; 
  
    @Column(nullable = false, columnDefinition = "TEXT") 
    private String conteudo; 
  
    @Column(length = 500) 
    private String resumo; 
  
    @Column(nullable = false) 
    private Boolean publicado = false; 
  
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "autor_id", nullable = false) 
    private Usuario autor; 
  
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, 
CascadeType.MERGE }) 
    @JoinTable( 
        name = "post_tags", 
        joinColumns = @JoinColumn(name = "post_id"), 
        inverseJoinColumns = @JoinColumn(name = "tag_id") 
    ) 
    private Set<Tag> tags = new HashSet<>(); 
  
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true) 
    private Set<Comentario> comentarios = new HashSet<>(); 
  
    @Column(name = "criado_em", nullable = false, updatable = false) 
    private LocalDateTime criadoEm; 
  
    @Column(name = "atualizado_em") 
    private LocalDateTime atualizadoEm; 
  
    @PrePersist 
    protected void onCreate() { 
        criadoEm = LocalDateTime.now(); 
        atualizadoEm = LocalDateTime.now(); 
    } 
  
    @PreUpdate 
    protected void onUpdate() { 
        atualizadoEm = LocalDateTime.now(); 
    } 
} 