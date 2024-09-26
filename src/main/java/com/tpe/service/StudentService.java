package com.tpe.service;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.dto.UpdateStudentDTO;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor//constructor injection
public class StudentService {

    private final StudentRepository repository;

    //2-tüm kayıtları listeleme
    public List<Student> getAllStudents() {
        return repository.findAll();// "FROM Student"
    }

    //4-öğrenciyi kaydetme
    public void saveStudent(Student student) {

        //student daha önce tabloya eklenmiş mi : tabloda aynı emaile sahip başka student var mı?
        //SELECT * FROM student WHERE email=student.getEmail()-->t/f

        if (repository.existsByEmail(student.getEmail())){
            //bu email daha önce kullanılmış-->hata fırlatalım
            throw new ConflictException("Email already exists!");
        }
        repository.save(student);//Insert into....

    }

    //6-id si verilen öğrenciyi bulma
    public Student getStudentById(Long id) {
        Student student=repository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("Student is not found by ID : "+id));

        return student;
    }

    //8-id si verilen öğrenciyi silme
    public void deleteStudentById(Long id) {//9999

        //repository.deleteById(id);//delete from student where id=9999;
        //bu idye sahip bir öğrenci (satır) yoksa???
        //özel bir mesaj ile özel bir exception fırlatmak istiyoruz

        //önce id si verilen öğrenciyi bulalım
        Student student=getStudentById(id);
        repository.delete(student);

    }

    //10-idsi verilen öğrencinin name,lastname ve emailini değiştirme
    public void updateStudent(Long id, UpdateStudentDTO studentDTO) {//id: 2 , email:harry@mail.com

        Student foundStudent=getStudentById(id);//1,"Jack","Sparrow","jack@mail.com",...

        //emailin unique olamsına engel var mı???
        //DTOda gelen email          tablodaki email
        //1-xxx@mail.com             YOK   V  (existsByEmail:false)  --> update
        //2-harry@mail.com           başka bir öğrenciye ait X (existsByEmail:true)--> ConflictException
        //3-jack@mail.com            kendisine ait V (existsByEmail:true) --> bu bir çakışma değil

        //istek ile gönderilen email daha önce kullanılmış mı?
        boolean existsEmail=repository.existsByEmail(studentDTO.getEmail());
        if (existsEmail && !foundStudent.getEmail().equals(studentDTO.getEmail())){
            //çakışma var
            throw new ConflictException("Email already exists!!!");
        }

        foundStudent.setName(studentDTO.getName());
        foundStudent.setLastName(studentDTO.getLastName());
        foundStudent.setEmail(studentDTO.getEmail());

        repository.save(foundStudent);//saveOrUpdate gibi çalışır.
    }

    //12-tablodaki tüm kayıtlardan istenen öğrenci sayfasını getirme
    public Page<Student> getAllStudentsPaging(Pageable pageable) {

        return repository.findAll(pageable);
        //istenen bilgiler verilirse tüm kayıtlardan sadece ilgili sayfayı getirir
        //istenen bilgileri pageable ile toplu olarka verebiliriz:sayfaNo,
        //                                                        her sayfada kayıt sayısı
        //                                                        sıralama bilgisi(hangi özellik,hangi yönde)
    }

    //14-grade ile öğrencileri filtreleme
    public List<Student> getAllStudentByGrade(Integer grade) {

        //select * from student where grade=100
      // return repository.findAllByGrade(grade);

      return repository.filterStudentsByGrade(grade);

    }

    //16-ödev
    public List<Student> getAllStudentByLastName(String lastName) {
        return repository.findAllByLastName(lastName);
    }

    //18-a: DB den id si verilen entityi getirip DTOya taşıyalım
    // öğrencinin bazı fieldlarını DTO olarak getirme
    public StudentDTO getStudentInfoById(Long id) {

        Student foundStudent=getStudentById(id);

//        StudentDTO studentDTO=
//                new StudentDTO(foundStudent.getName(),foundStudent.getLastName(), foundStudent.getGrade());
        //student(entity)--->studentDTO(DTO)

        StudentDTO studentDTO=new StudentDTO(foundStudent);

        return studentDTO;
    }

    //18-b:DB(tablodan)den direkt DTO(name,lastname,grade) çekme
    public StudentDTO getStudentInfoByDTO(Long id) {

        StudentDTO studentDTO=repository.findStudentDTOById(id).
                orElseThrow(()->new ResourceNotFoundException("Student Info is Not Found By Id: "+id));

        return studentDTO;
    }

    public List<Student> getStudentsSearching(String word) {

        //return repository.findByNameContains(word);
        word="%"+word+"%";
        return repository.findByNameLike(word);
    }


    public List<Student> getAllStudentByNameOrLastName(String word) {
        return repository.findByNameOrLastName(word,word);
    }
}