package by.pvt.dao;

import by.pvt.dao.impl.StudentDao;
import by.pvt.pojo.Course;
import by.pvt.pojo.Student;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Date;
import java.util.List;

import static by.pvt.util.HibernateUtil.getInstance;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StudentDaoTest {
    private StudentDao studentDao;

    private static Student createTestData(Long id) {
        char gender;
        Student student = new Student();
        student.setId(id);
        student.setFirstName("Name" + id);
        student.setSecondName("Surname" + id);
        if (id % 2 == 0) {
            gender = 'm';
        } else {
            gender = 'f';
        }
        student.setGender(gender);
        student.setDateOfBirth(new Date());
        return student;
    }

    @Before
    public void setUp() {
        studentDao = new StudentDao();
    }

    @Test
    public void testASave() {
        Session session = null;
        Student student1 = createTestData(1L);
        Student student2 = createTestData(2L);
        try {
            //save student in DB
            session = getInstance().getSession();
            studentDao.setSession(session);
            studentDao.save(student1);
            studentDao.save(student2);
            //get list of students
            List<Course> studentList = session.createQuery("from student").list();
            assertEquals(2, studentList.size());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testBGetById() {
        Session session = null;
        Long id = 1L;
        try {
            session = getInstance().getSession();
            studentDao.setSession(session);
            Student student = studentDao.getById(id);
            assertEquals("Name1", student.getFirstName());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testCGetList() {
        Session session = null;
        try {
            session = getInstance().getSession();
            studentDao.setSession(session);
            List<Student> students = studentDao.getList();
            assertEquals(2, students.size());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    @Test
    public void testDDelete() {
        Session session = null;
        Long studentIdForDalete = 1L;
        try {
            session = getInstance().getSession();
            studentDao.setSession(session);
            studentDao.delete(studentIdForDalete);
            List<Student> studentListAfterDelete = session.createQuery("from student").list();
            assertEquals(1, studentListAfterDelete.size());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testEUpdate() {
        Session session = null;
        Student student = new Student();
        String newSurname = "SurnameNew";
        student.setSecondName(newSurname);
        long idForUpdate = 2L;
        try {
            session = getInstance().getSession();
            studentDao.setSession(session);
            studentDao.update(idForUpdate, student);
            Student student1 = studentDao.getById(idForUpdate);
            String expectedSurname = student1.getSecondName();
            assertEquals(newSurname, expectedSurname);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}