package com.softserve.actent.model.entity;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.constant.NumberConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = ExceptionMessages.IMAGE_NO_FILEPATH)
    @Column(name = "file_path", nullable = false)
    private String filePath;

//    @NonNull
//    @NotBlank(message = ExceptionMessages.IMAGE_NO_HASH)
//    @Column(nullable = false, unique = true, length = NumberConstants.HASH_LENGTH)
//    @Size(min = NumberConstants.HASH_LENGTH, max = NumberConstants.HASH_LENGTH,
//            message = ExceptionMessages.IMAGE_INAPPROPRIATE_HASH_LENGTH)
//    private String hash;
}
