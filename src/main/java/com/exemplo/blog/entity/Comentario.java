// entity/Comentario.java 
package com.exemplo.blog.entity; 
  
import jakarta.persistence.*; 
import lombok.*; 
import java.time.LocalDateTime; 
  
@Entity 
@Table(name = "comentarios") 
@Getter @Setter 
@NoArgsConstructor @AllArgsConstructor @Builder 
public class Comentario { 
  
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
  
    @Column(nullable = false, columnDefinition = "TEXT") 
    private String conteudo; 
  
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "post_id", nullable = false) 
    private Post post; 
  
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "autor_id", nullable = false) 
    private Usuario autor; 
  
    @Column(name = "criado_em", nullable = false, updatable = false) 
    private LocalDateTime criadoEm; 
  
    @PrePersist 
    protected void onCreate() { 
        criadoEm = LocalDateTime.now(); 
    } 
} 