package by.pvt.dao.impl;

import by.pvt.dao.AbstractDao;
import by.pvt.pojo.Student;


public class StudentDao extends AbstractDao<Student> {

    public StudentDao() {
        super(Student.class);
    }

    @Override
    protected Student fillEntity(Student node, Student updateInfo) {
        node.setSecondName(updateInfo.getSecondName());
        return node;
    }
}
