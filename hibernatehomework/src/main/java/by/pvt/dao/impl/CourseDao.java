package by.pvt.dao.impl;

import by.pvt.dao.AbstractDao;
import by.pvt.pojo.Course;
import by.pvt.pojo.Student;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Set;
import java.util.logging.Level;

public class CourseDao extends AbstractDao<Course> {

    public CourseDao() {
        super(Course.class);
    }

    @Override
    protected Course fillEntity(Course node, Course updateInfo) {
        node.setCourseName(updateInfo.getCourseName());
        return node;
    }

    public Set<Student> findRegistratedOnCourse(String courseTitle) {
        Set<Student> registratedOnCourse;
        Query query = getSession().createQuery("FROM course WHERE courseName=:courseName");
        query.setParameter("courseName", courseTitle);
        Course course = (Course) query.uniqueResult();
        registratedOnCourse = course.getStudents();
        return registratedOnCourse;
    }

    public void addStudent(long courseId, Student student){
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            Course course = getSession().get(Course.class, courseId);
            course.addStudent(student);
            getSession().saveOrUpdate(course);
            tx.commit();
        } catch (HibernateException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            if(tx!=null) {
                tx.rollback();
            }
            throw  e;
        }
    }


    public void removeStudent(Long courseId, String studentSecondName){
        Transaction tx = null;
        Student student;
        try {
            tx = getSession().beginTransaction();
            Course course = getSession().get(Course.class, courseId);
            Query query = getSession().getNamedQuery("findStudentByLastName");
            query.setParameter("secondName", studentSecondName);
            student = (Student) query.uniqueResult();
            course.removeStudent(student);
            getSession().save(course);
            tx.commit();
        }catch (HibernateException e){
            log.log(Level.SEVERE, e.getMessage(), e);
            if(tx!=null) {
                tx.rollback();
            }
            throw  e;
        }
    }
}
