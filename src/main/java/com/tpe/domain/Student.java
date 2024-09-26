package com.tpe.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter//tüm fieldlar için getter metodunun tanımlanmasını sağlar
@Setter//tüm fieldlar için setter metodunun tanımlanmasını sağlar
@AllArgsConstructor
@NoArgsConstructor

//@RequiredArgsConstructor:final ile işaretlenen fieldları parametre olarak alan constructor

/*
    public Student(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

 */
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "name can not be blank!")//request anında doğrulama
    @Size(min = 2,max = 50,message = "name must be between 2 and 50")//request anında doğrulama
    @Column(nullable = false)
    /*final*/ private String name;

    @NotBlank(message = "lastname can not be blank!")//request anında doğrulama
    @Size(min = 2,max = 50,message = "lastname must be between 2 and 50")//request anında doğrulama
    @Column(nullable = false)
    /*final*/ private String lastName;

    @NotNull(message = "grade can not be null")
    @Column(nullable = false)
    private Integer grade;

    @Column(unique = true)
    @Email(message = "please provide valid email!")//aaa@bbb.ccc email formatında olmasını doğrular
    //@Pattern()
    private String email;

    @Setter(AccessLevel.NONE)
    private LocalDateTime createDate=LocalDateTime.now();

    @OneToMany(mappedBy = "student")
    private List<Book> bookList=new ArrayList<>();

    @OneToOne
    private User user;

    //getter-setter:boilerplate code


}
