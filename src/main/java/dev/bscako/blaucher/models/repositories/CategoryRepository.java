/**
 *
 */
package dev.bscako.blaucher.models.repositories;

import dev.bscako.blaucher.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author BSCAKO
 *
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByName(String name);

}
