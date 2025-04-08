package com.codesignx.dentaldemo.models;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString(exclude = {"appointments"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private String telephone;
    
    @Column(nullable = false)
    private String telephonePrefix;
    
    @Column(nullable = false)
    private boolean active = true;
    
    // 数据库系统字段
    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", updatable = false, insertable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "status")
    private boolean status;
    
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Appointment> appointments;
}