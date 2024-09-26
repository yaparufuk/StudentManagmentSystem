package com.tpe.domain;

//1S-->MBook -->OneToMany
//MB-->1S  --->ManytoOne

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Book {//Many

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @JsonProperty("bookName")
    //sadece JSON formatında fieldın belirtilen key ile gösterilmesini sağlar
    private String name;

    @ManyToOne
    @JsonIgnore
    //bu fieldı JSON formatında ignore et(görmezden gel)
    private Student student;

}
