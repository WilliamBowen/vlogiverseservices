package com.bowen.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("" +
           "SELECT CASE WHEN COUNT(p) > 0 THEN " +
           "TRUE ELSE FALSE END " +
           "FROM Person p " +
           "WHERE p.email = ?1"
    )
    boolean selectExistsEmail(String email);

    @Query("" +
            "SELECT CASE WHEN COUNT(p) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Person p " +
            "WHERE p.username = ?1"
    )
    boolean selectExistsUsername(String username);

}
