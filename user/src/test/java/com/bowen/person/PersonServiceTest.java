package com.bowen.person;


import com.bowen.helper.EmailValidator;
import com.bowen.person.exception.BadRequestException;
import com.bowen.person.exception.PersonNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @Mock
    EmailValidator emailValidator;
    private PersonService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PersonService(personRepository, emailValidator);
    }

    @Test
    void canGetAllPersons() {
        // when
        underTest.getAllPersons();
        // then
        verify(personRepository).findAll();
    }
    @Test
    void canRegisterPerson() {
        // given
        Person person = new Person(
                "Random",
                "random@gmail.com"
        );
        PersonRegistrationRequest request = new PersonRegistrationRequest(
                "Random",
                "random@gmail.com"
        );
        given(emailValidator.test(anyString())).willReturn(true);

        // when
        underTest.registerPerson(request);

        //then
        ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(personArgumentCaptor.capture());
        Person capturedPerson = personArgumentCaptor.getValue();
        assertThat(capturedPerson).isEqualTo(person);
    }

    @Test
    void willThrowWhenEmailIsNotValid() {
        // given
        String email = "randomgmail.com";
        PersonRegistrationRequest request = new PersonRegistrationRequest(
                "Random",
                email
        );
        given(emailValidator.test(anyString())).willReturn(false);

        // then
        assertThatThrownBy(() -> underTest.registerPerson(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + email + " is not valid");
        verify(personRepository, never()).save(any());
    }
    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        String email = "random@gmail.com";
        PersonRegistrationRequest request = new PersonRegistrationRequest(
                "Random",
                email
        );
        given(personRepository.selectExistsEmail(anyString())).willReturn(true);
        given(emailValidator.test(anyString())).willReturn(true);

        // then
        assertThatThrownBy(() -> underTest.registerPerson(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + email + " taken");
        verify(personRepository, never()).save(any());
    }

    @Test
    void willThrowWhenUsernameIsTaken() {
        // given
        String username = "Random";
        PersonRegistrationRequest request = new PersonRegistrationRequest(
                username,
                "random@gmail.com"
        );
        given(personRepository.selectExistsUsername(anyString())).willReturn(true);
        given(emailValidator.test(anyString())).willReturn(true);

        // then
        assertThatThrownBy(() -> underTest.registerPerson(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Username " + username + " taken");
        verify(personRepository, never()).save(any());
    }


    @Test
    void canDeletePerson() {
        // given
        int id = 10;
        given(personRepository.existsById(id)).willReturn(true);
        // when
        underTest.deletePerson(id);

        // then
        verify(personRepository).deleteById(id);
    }

    @Test
    void willThrowWhenDeletePersonNotFound() {
        // given
        int id = 10;
        given(personRepository.existsById(id))
                .willReturn(false);
        // when
        // then
        assertThatThrownBy(() -> underTest.deletePerson(id))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessageContaining("Person with id " + id + " does not exist");
        verify(personRepository, never()).deleteById(any());
    }

}
