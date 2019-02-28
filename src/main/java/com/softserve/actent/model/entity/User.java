package com.softserve.actent.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class User {
    @Id
//    @GenericGenerator(name = "idGenerator", strategy = "increment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "first_name")
    private String firstName;

    @NonNull
    @Column(name = "last_name")
    private String lastName;

    // TODO: make unique
    @NonNull
    private String login;
//
//    @NonNull
//    private String password;
//
//    @ManyToOne
//    private Chat chat;
}
