package by.pvt.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "student")
@NamedQuery(name = "findStudentByLastName",
        query = "select s from student s where s.secondName = :secondName")
public class Student implements Serializable {
    private static final long SerialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "id", strategy = "increment")
    @GeneratedValue(generator = "id")
    private long id;

    @Column
    private String firstName;

    @Column
    private String secondName;

    @Column
    private Date dateOfBirth;

    @Column
    private char gender;

    @ManyToMany(mappedBy = "students")
    private Set<Course> courses = new HashSet<>();

    public Student() {
    }

    public Student(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != student.id) return false;
        if (gender != student.gender) return false;
        if (!firstName.equals(student.firstName)) return false;
        if (!secondName.equals(student.secondName)) return false;
        return dateOfBirth != null ? dateOfBirth.equals(student.dateOfBirth) : student.dateOfBirth == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + firstName.hashCode();
        result = 31 * result + secondName.hashCode();
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (int) gender;
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", courses=" + courses +
                '}';
    }
}