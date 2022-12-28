package com.bowen.person;

import com.bowen.helper.EmailValidator;
import com.bowen.person.exception.BadRequestException;
import com.bowen.person.exception.PersonNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PersonService {
    @Autowired
    private final PersonRepository personRepository;
    @Autowired
    private final EmailValidator emailValidator;
    public Person registerPerson(PersonRegistrationRequest request) {

        // check if email is valid
        Boolean isValidEmail = emailValidator.test(request.email());
        if(!isValidEmail) {
            throw new BadRequestException("Email " + request.email() + " is not valid");
        }
        // check if email is not already registered
        Boolean existsEmail = personRepository.selectExistsEmail(request.email());
        if(existsEmail) {
            throw new BadRequestException("Email " + request.email() + " taken");
        }
        // check if username is not taken
        Boolean existsUsername = personRepository.selectExistsUsername(request.username());
        if(existsUsername) {
            throw new BadRequestException("Username " + request.username() + " taken");
        }

        Person person = Person.builder()
                .username(request.username())
                .email(request.email())
                .build();
        return personRepository.save(person);
    }

    public boolean doesPersonExist(Integer personId) {
        // todo: check database to verify user does exist
        return true;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public void deletePerson(int id) {
        if(!personRepository.existsById(id)) {
            throw new PersonNotFoundException("Person with id " + id + " does not exist");
        }
        personRepository.deleteById(id);
    }
}
