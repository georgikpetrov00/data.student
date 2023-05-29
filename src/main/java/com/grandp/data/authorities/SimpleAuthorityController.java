package com.grandp.data.authorities;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authority")
public class SimpleAuthorityController {

    @Autowired
    private SimpleAuthorityService simpleAuthorityService;

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleAuthority> createAuthority(@RequestBody SimpleAuthority simpleAuthority) {
        SimpleAuthority savedSimpleAuthority = simpleAuthorityService.createAuthority(simpleAuthority);
        return ResponseEntity.ok(savedSimpleAuthority);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<SimpleAuthority> getAuthorityById(@PathVariable("id") Long id) {
        SimpleAuthority simpleAuthority = simpleAuthorityService.getAuthorityById(id);
        return ResponseEntity.ok(simpleAuthority);
    }

    @GetMapping
    public ResponseEntity<List<SimpleAuthority>> getAllAuthorities() {
        List<SimpleAuthority> simpleAuthorities = simpleAuthorityService.getAllAuthorities();
        return ResponseEntity.ok(simpleAuthorities);
    }

    @PostMapping(path = "/update/{name}")
    public ResponseEntity<?> updateAuthority(@PathVariable String name,
                                             @RequestParam(required = false) String newName,
                                             @RequestParam(required = false) String description) {
        Optional<SimpleAuthority> authorityOpt = simpleAuthorityService.getAuthorityByName(name);
        if (authorityOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Authority with Name '" + name + "' does not exist.");
        }
        SimpleAuthority authority = authorityOpt.get();

        SimpleAuthority updatedAuthority = simpleAuthorityService.updateAuthority(authority, newName, description);
        return ResponseEntity.ok(updatedAuthority);
    }

}
