package com.tpe.domain;

import com.tpe.domain.enums.RoleType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Enumerated(EnumType.STRING)
    //enum sabitlerinin DB de String olarak kaydedilmesini sağlar
    @Column(nullable = false)
    private RoleType type;//ROLE_STUDENT, ROLE_ADMIN
}
