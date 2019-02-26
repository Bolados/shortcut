/**
 *
 */
package dev.bscako.blaucher.models.repositories;

import dev.bscako.blaucher.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author BSCAKO
 *
 */
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByExePath(String exepath);
}
