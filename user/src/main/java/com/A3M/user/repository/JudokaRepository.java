package com.A3M.user.repository;

import com.A3M.user.model.Judoka;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JudokaRepository extends JpaRepository<Judoka, Long> {
}
