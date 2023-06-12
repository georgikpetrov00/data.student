package com.grandp.data.entity.authority;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SimpleAuthorityRepository extends JpaRepository<SimpleAuthority, Long> {

    public Optional<SimpleAuthority> getSimpleAuthorityByAuthorityName(String roleName);
}