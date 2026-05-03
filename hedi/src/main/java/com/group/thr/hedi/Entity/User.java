package com.group.thr.hedi.Entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String hashed_Password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role; 

    @Enumerated(EnumType.STRING)
    private AccountStatus status; 
    
    @Column(unique = true, nullable = true)
    private String oauth_Id;
    
    @Column(nullable = true)
    private String oauth_Provider;  // e.g., "google", "github"

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum Role {
        USER, ADMIN
    }

    public enum AccountStatus {
        ACTIVE, LOCKED, PENDING_VERIFICATION
    }
}