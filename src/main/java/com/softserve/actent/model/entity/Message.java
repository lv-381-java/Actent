package com.softserve.actent.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Data
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private Long sendTime;

    @NonNull
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    private MessageBody messageBody;

    @NonNull
    @ManyToOne
    private User sender;

    @NonNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Chat chat;

}
