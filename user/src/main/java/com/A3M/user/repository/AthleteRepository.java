package com.A3M.user.repository;

import com.A3M.user.dto.athlete.response.AthleteDto;
import com.A3M.user.model.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    @Query("SELECT a FROM Athlete a WHERE LOWER(a.firstName) LIKE LOWER(CONCAT(:substring, '%')) " +
            "OR LOWER(a.firstName) LIKE LOWER(CONCAT('%', :substring, '%'))")
    List<Athlete> findByFirstNameStartingWithOrContaining(@Param("substring") String substring);

    @Query("SELECT a FROM Athlete a WHERE LOWER(a.lastName) LIKE LOWER(CONCAT(:substring, '%')) " +
            "OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :substring, '%'))")
    List<Athlete> findByLastNameStartingWithOrContaining(@Param("substring") String substring);

    @Query("SELECT a FROM Athlete a WHERE " +
            "YEAR(CURRENT_DATE) - YEAR(a.birthDate) - " +
            "CASE WHEN MONTH(CURRENT_DATE) < MONTH(a.birthDate) OR " +
            "(MONTH(CURRENT_DATE) = MONTH(a.birthDate) AND DAY(CURRENT_DATE) < DAY(a.birthDate)) " +
            "THEN 1 ELSE 0 END BETWEEN :minAge AND :maxAge")
    List<Athlete> findByAgeBetween(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
}
