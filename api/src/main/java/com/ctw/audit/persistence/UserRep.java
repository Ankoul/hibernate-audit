package com.ctw.audit.persistence;

import com.ctw.audit.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRep extends CrudRepository<User, String> {

    boolean existsByUsername(String username);
    User findByUsername(String username);

//    @Query("select distinct user from User user " +
//            "left join SchoolEntity school on school.calendar.id = :calendarId " +
//            "left join StudentEntity student on student.schoolId = school.id " +
//            "left join student.responsibles resp " +
//            "where user.schoolId = school.id or resp.id = user.id")
//    List<User> findAllBySchoolCalendarId(@Param("calendarId") String calendarId);
//
//    @Query("select distinct student.responsibles from StudentEntity student " +
//            "inner join ClassEntity clazz on clazz.calendar.id = :calendarId " +
//            "where student.classId = clazz.id")
//    List<User> findAllByClassCalendarId(@Param("calendarId") String calendarId);
}
