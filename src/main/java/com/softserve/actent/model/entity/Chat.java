package com.softserve.actent.model.entity;

import com.softserve.actent.constant.StringConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "chat")
    private List<Message> messages;

    @NonNull
    @NotNull(message = StringConstants.CHAT_TYPE_NOT_BE_BLANK)
    @Enumerated(EnumType.ORDINAL)
    private ChatType type;

    @NonNull
    @OneToMany(fetch=FetchType.LAZY,
            mappedBy = "id")
    private Set<User> banned = new HashSet<>();

}
