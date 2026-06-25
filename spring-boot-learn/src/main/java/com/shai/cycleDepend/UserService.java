package com.shai.cycleDepend;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Lazy
    @Autowired
    private TeacherService teacherService;
}
