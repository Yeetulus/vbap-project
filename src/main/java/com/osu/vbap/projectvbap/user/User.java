package com.osu.vbap.projectvbap.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osu.vbap.projectvbap.library.loan.Loan;
import com.osu.vbap.projectvbap.library.reservation.Reservation;
import com.osu.vbap.projectvbap.library.review.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "library_users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true)
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private Set<Loan> loans;
    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private Set<Reservation> reservations;
    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user", "book"})
    private Set<Review> reviews;


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}