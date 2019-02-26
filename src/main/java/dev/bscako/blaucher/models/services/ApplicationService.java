/**
 *
 */
package dev.bscako.blaucher.models.services;

import dev.bscako.blaucher.models.Application;
import dev.bscako.blaucher.models.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BSCAKO
 *
 */

@Service
public class ApplicationService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private ApplicationRepository applicationRepository;

    /**
     * @return the applicationRepository
     */
    public ApplicationRepository getApplicationRepository() {
        return this.applicationRepository;
    }

    public List<Application> findAllApplications() {
        return this.applicationRepository.findAll().stream().distinct().collect(Collectors.toList());
    }

    public Application findApplicationByExePath(String exepath) {
        List<Application> applications = this.applicationRepository.findByExePath(exepath);
        if ((!applications.isEmpty()) && (applications.size() == 1)) {
            return applications.get(applications.size() - 1);
        }
        return null;

    }


}
