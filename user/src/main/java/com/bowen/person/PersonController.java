package com.bowen.person;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;

    @PostMapping
    public ResponseEntity<Person> registerPerson(@RequestBody PersonRegistrationRequest personRegistrationRequest) {
        log.info("new user registration {}", personRegistrationRequest);
        Person person = personService.registerPerson(personRegistrationRequest);
        try{
            return ResponseEntity
                    .created(new URI("/api/v1/users/" + person.getUsername()))
                    .body(person);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // TODO: This method is just for testing
    @GetMapping(path= "{personId}")
    public PersonExistsResponse doesExist(@PathVariable("personId") Integer personId) {
        boolean doesExist = personService.doesPersonExist(personId);
        return new PersonExistsResponse(doesExist);
    }

}
