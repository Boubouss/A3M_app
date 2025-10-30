package com.A3M.user.repository;

import com.A3M.user.model.Chaperone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChaperoneRepository extends JpaRepository<Chaperone, Long>{
    @Query("SELECT c FROM Chaperone c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT(:substring, '%')) " +
            "OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :substring, '%'))")
    List<Chaperone> findByFirstNameStartingWithOrContaining(@Param("substring") String substring);

    @Query("SELECT c FROM Chaperone c WHERE LOWER(c.lastName) LIKE LOWER(CONCAT(:substring, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :substring, '%'))")
    List<Chaperone> findByLastNameStartingWithOrContaining(@Param("substring") String substring);
}