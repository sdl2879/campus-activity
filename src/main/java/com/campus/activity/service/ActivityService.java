package com.campus.activity.service;

import java.util.List;

public interface ActivityService {
    <Activity> List<Activity> recommendBySubject(String subjectType);

    String getStudentSubjectType(Long studentId);
}
