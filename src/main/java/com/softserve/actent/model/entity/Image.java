package com.softserve.actent.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = "File path can`t be empty.")
    @Column(name = "file_path", nullable = false,  unique = true, length = 256)
    @Length(max = 256, message = "Too long")
    private String filePath;

    @NonNull
    @NotBlank(message = "hash can`t be empty.")
    @Column(nullable = false, unique = true, length = 256)
    @Length(min = 256, max = 256, message = "Hash must be of exact length 256")
    private String hash;

    @ManyToOne(fetch = FetchType.LAZY)
    private MessageBody messageBody;
}
