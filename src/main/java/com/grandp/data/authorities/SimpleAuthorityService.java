package com.grandp.data.authorities;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import org.thymeleaf.expression.Sets;

@Service
public class SimpleAuthorityService {

    @Autowired
    private SimpleAuthorityRepository simpleAuthorityRepository;

    public SimpleAuthority createAuthority(SimpleAuthority simpleAuthority) {
        return simpleAuthorityRepository.save(simpleAuthority);
    }

    public SimpleAuthority getAuthorityById(Long id) {
        return simpleAuthorityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("SimpleAuthority not found with id " + id));
    }

    public List<SimpleAuthority> getAllAuthorities() {
        return simpleAuthorityRepository.findAll();
    }

    public Optional<SimpleAuthority> getAuthorityByName(String roleName) {
        Optional<SimpleAuthority> role = simpleAuthorityRepository.getSimpleAuthorityByAuthorityName(roleName);
        return role;
    }

    public SimpleAuthority updateAuthority(SimpleAuthority authority, String newName, String description) {

        setAuthorityName(authority, newName);

        if (description != null) {
            authority.setDescription(description);
        }

        SimpleAuthority updatedAuthority = simpleAuthorityRepository.save(authority);

        return updatedAuthority;
    }

     /** Sets the name of an Authority.
      * If the Authority is SystemAuthority:
      * - it cannot be changed;
      * - returns false;
      *
      * If the new name is null: return false
      * If the new name is valid: return true
     */
     private boolean setAuthorityName(SimpleAuthority authority, String newName) {
        if (isSystemAuthority(authority)) {
            //TODO: Trace that System Authority Names cannot be changed

            return false;
        }

        if (newName != null) {
            authority.setAuthorityName(newName);
        } else {
            // Trace that newName is null and cannot be set

            return false;
        }

        return true;
    }

    private boolean isSystemAuthority(SimpleAuthority authority) {
        return authority.equals(SimpleAuthority.ADMINISTRATOR)
                || authority.equals(SimpleAuthority.GUEST)
                || authority.equals(SimpleAuthority.STUDENT)
                || authority.equals(SimpleAuthority.TEACHER);
    }

}