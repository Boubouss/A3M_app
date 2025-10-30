package com.A3M.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import com.A3M.user.enums.EBelt;

@Entity
@Table(name = "athlete")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"user"})
public class Athlete {
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

    @Column(unique = true)
    private Long athleteDetailsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
