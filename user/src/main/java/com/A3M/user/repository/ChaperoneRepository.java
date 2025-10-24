package com.A3M.user.repository;

import com.A3M.user.model.Chaperone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChaperoneRepository extends JpaRepository<Chaperone, Long>{
}