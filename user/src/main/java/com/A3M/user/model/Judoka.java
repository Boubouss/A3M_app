package com.A3M.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import com.A3M.user.enums.EBelt;

@Entity
@Table(name = "judoka")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Judoka {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private Boolean gender;

    @Column(unique = true)
    private Long licenseId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EBelt beltLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
