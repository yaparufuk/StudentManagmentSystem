package com.tpe.controller;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.dto.UpdateStudentDTO;
import com.tpe.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController//responsebody:metodun dönüş değerini JSON formatında cevap olarak hazırlar
@RequiredArgsConstructor
//bu classta Restful servisler yazılacak,
// requestlere karşılık responselar oluşturulacak
@RequestMapping("/students")//http://localhost:8080/students/.....
public class StudentController {


    Logger logger= LoggerFactory.getLogger(StudentController.class);




    /*
    clienttan 3 şekilde data alabiliriz
    1-request body ile JSON formatında
    2-urlde path param
    3-urlde query param
     */


    private final StudentService service;

    //Spring Bootu selamlama requesti:)
    //http://localhost:8080/students/greet + GET
    @GetMapping("/greet")
    public String greet() {
        return "Hello Spring Boot:)";
    }

    //1-tüm öğrencileri listeleyelim : READ
    //Request : http://localhost:8080/students + GET
    //Response: Tüm Öğrencilerin Listesini + 200 : OK (Http Status Kodu)

    @PreAuthorize("hasRole('ADMIN')")//ROLE_ADMIN
    //bu requesti sadece Admin yapabilsin
    @GetMapping
    // @ResponseBody--> RestController içinde var, bu sebeple gerek kalmadı!!!
    public ResponseEntity<List<Student>> listAllStudents() {
        //tablodan tüm öğrencileri getirelim
        List<Student> studentList = service.getAllStudents();
        return new ResponseEntity<>(studentList, HttpStatus.OK);//200
    }
    //Response entity : body + status kodu
    //jackson : objeler --> JSON formatına mapler
    //          JSON formatı --> obje


    //3-öğrenci ekleme : CREATE
    //Request : http://localhost:8080/students + POST + body(JSON)
    /*
    {
     "name":"Jack",
     "lastName":"Sparrow",
     "email":"jack@mail.com",
     "grade":98
    }
     */
    //Response : başarılı mesaj + 201 (CREATED)
    @PostMapping
    public ResponseEntity<String> createStudent(@Valid @RequestBody Student student) {

        service.saveStudent(student);

        return new ResponseEntity<>("Student is created successfully...", HttpStatus.CREATED);//201
    }
    //@RequestBody : requestin bodysini almamızı sağlar
    //jackson:bodydeki JSON formatını --> Student objesine dönüştürüyor


    //5-query param ile id si verilen öğrenciyi getirme
    //request: http://localhost:8080/students/query?id=1 + GET
    //response : student + 200
    @GetMapping("/query")
    public ResponseEntity<Student> getStudent(@RequestParam("id") Long id) {

        Student student = service.getStudentById(id);

        return new ResponseEntity<>(student, HttpStatus.OK);//200
    }

    //ÖDEV:(Alternatif)5-path param ile id si verilen öğrenciyi getirme
    //request: http://localhost:8080/students/1 + GET
    //response : student + 200
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentByPath(@PathVariable("id") Long id) {

        Student student = service.getStudentById(id);

        return new ResponseEntity<>(student, HttpStatus.OK);//200
    }

    //7-path param ile id si verilen öğrenciyi silme
    //request: http://localhost:8080/students/1 + DELETE
    //response : başarılı mesaj + 200
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id){

        service.deleteStudentById(id);

        //return new ResponseEntity<>("Student is deleted successfully...",HttpStatus.OK);//200
        return ResponseEntity.ok("Student is deleted successfully...");//200
    }

    //9-path param ile id si verilen öğrenciyi güncelleme:(name,lastName,email)
    //request: http://localhost:8080/students/1 + PUT/PATCH + BODY(JSON)
    //response: başarılı mesaj + 201
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable Long id,
                                                @Valid @RequestBody UpdateStudentDTO studentDTO){

        service.updateStudent(id,studentDTO);

        return new ResponseEntity<>("Student is updated successfully...",HttpStatus.CREATED);//201
    }

    //11-tüm öğrencileri listeleme : READ
    //tüm kayıtları page page(sayfa sayfa) gösterelim
    //request :
    //http://localhost:8080/students/page?
    //                               page=3&
    //                               size=20&
    //                               sort=name&
    //                               direction=DESC(ASC) + GET

    // 1 | 2 | 3 | 4 .....next
    @GetMapping("/page")
    public ResponseEntity<Page<Student>> getAllStudentsByPage(@RequestParam("page") int pageNo,
                                                              @RequestParam("size") int size,
                                                              @RequestParam("sort") String property,
                                                              @RequestParam("direction")Sort.Direction direction){

        Pageable pageable= PageRequest.of(pageNo,size,Sort.by(direction,property));
        //findAll metodunun sayfa getirmesi için gerekli olan bilgileri
        //pageable tipinde verebiliriz.

        Page<Student> studentPage=service.getAllStudentsPaging(pageable);

        return new ResponseEntity<>(studentPage,HttpStatus.OK);
    }

    //13-grade ile öğrencileri filtreleyelim
    //request:http://localhost:8080/students/grade/100 + GET
    //response : 100 grade e sahip olan öğrenci listesi + 200
    @GetMapping("/grade/{grade}")
    public ResponseEntity<List<Student>> getAllStudentByGrade(@PathVariable Integer grade){

        List<Student> studentList=service.getAllStudentByGrade(grade);

        return new ResponseEntity<>(studentList,HttpStatus.OK);//200
    }

    //ÖDEVVVV:)
    //JPA reponun hazır metodları
    //JPQL/SQL ile custom sorgu
    //15-lastname ile öğrencileri filtreleyelim
    //request:http://localhost:8080/students/lastname?lastname=Potter + GET
    //response : lastname e sahip olan öğrenci listesi + 200
    @GetMapping("/lastname")
    public ResponseEntity<List<Student>> getStudentsByLastName(@RequestParam String lastName){

        List<Student> studentList=service.getAllStudentByLastName(lastName);

        return ResponseEntity.ok(studentList);//200
    }

    //Meraklısına ÖDEVVV:) isim veya soyisme göre filtreleme
    //request:http://localhost:8080/students/search?word=harry + GET
    @GetMapping("/search")
    public ResponseEntity<List<Student>> getAllStudentByNameOrLastName(@RequestParam("word") String word){
        List<Student> studentList = service.getAllStudentByNameOrLastName(word);
        return ResponseEntity.ok(studentList);

    }


    //17-id si verilen öğrencinin name,lastName ve grade getirme
    //request:http://localhost:8080/students/info/2 + GET
    //response : id si verilen öğrencinin sadece 3 fieldını DTO ile + 200
    @GetMapping("/info/{id}")
    public ResponseEntity<StudentDTO> getStudentInfo(@PathVariable Long id){

        //StudentDTO studentDTO=service.getStudentInfoById(id);
        StudentDTO studentDTO=service.getStudentInfoByDTO(id);

        logger.warn("-----servisten gelen DTO objesi----------"+studentDTO.getName());

        return ResponseEntity.ok(studentDTO);//200
    }

    //ÖDEVVV:) JPA reponun hazır metodunu türeterek veya
    //          JPQL/SQL ile custom sorgu yazarak
    //19-name içinde "al" hecesi geçen öğrencileri filtreleyelim: READ//ex:halil
    //http://localhost:8080/students/filter?word=al + GET

    @GetMapping("/filter")
    public ResponseEntity<List<Student>> getStudentInfo(@RequestParam String word){

        List<Student> students=service.getStudentsSearching(word);

    return ResponseEntity.ok(students);//200

    }


    //21-/http://localhost:8080/students/welcome + GET
    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request){

        logger.info("welcome isteğinin pathi: "+request.getServletPath());
        logger.info("welcome isteğinin http metodu : "+request.getMethod());

        return "Welcome:)";
    }






}