package by.pvt.dao;

import by.pvt.dao.impl.CourseDao;
import by.pvt.pojo.Course;
import by.pvt.pojo.Student;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static by.pvt.util.HibernateUtil.getInstance;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CourseDaoTest {
    private Session session;
    private CourseDao courseDao;

    @Before
    public void setUp() {
        courseDao = new CourseDao();
    }

    private static Course createTestData(long id, String courseName) {
        Course course = new Course();
        course.setId(id);
        course.setCourseName(courseName);
        return course;
    }

    @Test
    public void testASaveCourse() {
        //create course Physics
        Course course1 = createTestData(1L, "Physics");
        //create course Mathematics
        Course course2 = createTestData(2L, "Mathematics");
        //register students on course Physics
        HashSet<Student> studentsSet = new HashSet<>() {
            {
                this.add(new Student("Andrei", "Ivanov"));
                this.add(new Student("Alexander", "Alexandrov"));
                this.add(new Student("Ivan", "Ivankov"));
            }
        };
        course1.setStudents(studentsSet);

        try {
            session = getInstance().getSession();
            //save course Physics in DB
            courseDao.setSession(session);
            courseDao.save(course1);
            courseDao.save(course2);

            //get list of courses
            List<Course> courseList = session.createQuery("from course ").list();
            assertEquals(2, courseList.size());

            //find registered on course Physics
            Set<Student> studentSetResult =
                    courseDao.findRegistratedOnCourse("Physics");
            assertEquals(3, studentSetResult.size());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testBDeleteCourse() {
        try {
            //delete course Mathematics
            session = getInstance().getSession();
            courseDao.setSession(session);
            courseDao.delete(2L);

            //get list of courses using method getCourses() from CourseDao
            List<Course> courseList = courseDao.getList();
            assertEquals(1, courseList.size());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testCUpdateCourse() {
        Long idForUpdate = 1L;
        String newCourseName = "Physics and Mathematics";
        Course updateInfo = createTestData(0, newCourseName);
        try {
            //update course Physics
            session = getInstance().getSession();
            courseDao.setSession(session);
            courseDao.update(idForUpdate, updateInfo);
            //get list of courses
            List<Course> courseList = session.createQuery("from course").list();
            assertEquals(newCourseName, courseList.get(0).getCourseName());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testDAddStudent() {
        //data preparation
        Student student = new Student();
        student.setFirstName("Name1");
        student.setSecondName("Surname1");
        student.setGender('m');
        student.setDateOfBirth(new Date());
        long courseId = 1L;
        try {
            session = getInstance().getSession();
            courseDao.setSession(session);
            courseDao.addStudent(courseId, student);

            Course course = courseDao.getById(courseId);
            Set<Student> students = course.getStudents();
            assertEquals(4, students.size());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testERemoveStudent() {
        //data preparation
        long courseId = 1L;
        String secondName = "Surname1";
        try {
            session = getInstance().getSession();
            courseDao.setSession(session);
            courseDao.removeStudent(courseId, secondName);

            Course course = courseDao.getById(courseId);
            Set<Student> students = course.getStudents();
            assertEquals(3, students.size());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}