package com.A3M.user.services;

import com.A3M.user.model.Judoka;
import com.A3M.user.repository.JudokaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JudokaService {
    @Autowired
    private JudokaRepository repository;


}
