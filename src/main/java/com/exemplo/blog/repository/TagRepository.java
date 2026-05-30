// repository/TagRepository.java 
package com.exemplo.blog.repository; 


import org.springframework.data.jpa.repository.JpaRepository; 

import org.springframework.stereotype.Repository; 
  
import java.util.Optional;
import com.exemplo.blog.entity.Tag;

@Repository 
public interface TagRepository extends JpaRepository<Tag, Long> { 
    Optional<Tag> findByNome(String nome); 
} 
  
