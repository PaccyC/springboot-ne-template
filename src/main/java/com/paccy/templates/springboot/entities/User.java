package com.paccy.templates.springboot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paccy.templates.springboot.enums.EGender;
import com.paccy.templates.springboot.enums.ERole;
import com.paccy.templates.springboot.enums.EUserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_users")
public class User implements  UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true)
    private String email;

    @Column(unique = true,name = "phone_number")
    private String phoneNumber;

    private String password;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_picture_id")
    private File profilePicture;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EUserStatus userStatus = EUserStatus.PENDING;

    @Column(name = "activation_code")
    private String activationCode;

    @JsonIgnore
    @Column(name = "activation_code_expires_at")
    private LocalDateTime activationCodeExpiresAt;



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonLocked() {
        return  true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return  true;
    }
}
