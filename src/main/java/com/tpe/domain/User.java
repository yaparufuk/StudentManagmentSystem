package com.tpe.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false,length = 50)//db aşamasında kontol eder
    private String firstName;

    @Column(nullable = false,length = 50,unique = true)
    private String userName;

    @Column(nullable = false,length = 255)
    //password DB ye kaydedilmeden önce şifrelenecek o yüzden 255 seçtik
    private String password;

    @ManyToMany(fetch = FetchType.EAGER) // ssağ taraf many olduğu için defaıltu lazy.  User geldiğiğinde rol de gelsin dediğim için eager yaptık
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles=new HashSet<>(); //tekrarı önlemek için set seçtik


}
