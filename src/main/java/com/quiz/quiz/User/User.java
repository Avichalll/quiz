package com.quiz.quiz.User;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.quiz.quiz.Role.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_User")
@EntityListeners(AuditingEntityListener.class)

public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "FirstName should not be null")
    @NotEmpty(message = "FirstName should not be empty")
    @Size(min = 2, max = 50, message = "FirstName should be between 2 and 50 characters")

    private String firstname;
    @NotNull(message = "LastName should not be null")
    @NotEmpty(message = "LastName should not be empty")
    @Size(min = 2, max = 50, message = "LastName should be between 2 and 50 characters")
    private String lastname;

    @Column(name = "contact_Number", unique = true)
    @NotNull(message = "ContactNumber should not be null")
    @NotEmpty(message = "ContactNumber should not be empty")
    @Pattern(regexp = "\\d{10}", message = "Contact number should be a 10-digit number")
    private String contactNumber;

    @NotNull(message = "Password should not be null")
    @NotEmpty(message = "Password should not be empty")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;
    private boolean accountLocked;
    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Token> token;

    @CreatedDate
    private LocalDateTime lastmodifiedDate;

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return contactNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public String getName() {
        return contactNumber;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getName().name()));
    }

}