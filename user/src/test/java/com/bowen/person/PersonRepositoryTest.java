package com.bowen.person;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository underTest;
    @Autowired
    private TestEntityManager entityManager;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckWhenPersonEmailExists() {
        // given
        String email ="random@gmail.com";
        Person person= new Person(
                "random",
                email
        );
        underTest.save(person);

        // when
        boolean expected = underTest.selectExistsEmail(email);

        // then
        assertThat(expected).isTrue();

    }

    @Test
    void itShouldCheckWhenPersonEmailDoesNotExist() {
        // given
        String email = "random@gmail.com";

        // when
        boolean expected = underTest.selectExistsEmail(email);

        // then
        assertThat(expected).isFalse();
    }
}
