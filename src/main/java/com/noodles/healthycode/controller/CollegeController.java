package com.noodles.healthycode.controller;


import com.noodles.healthycode.common.lang.Result;
import com.noodles.healthycode.entity.College;
import com.noodles.healthycode.entity.Student;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/college")
public class CollegeController {

    @PostMapping("/save")
    private Result save (@Validated @RequestBody College college){

        return Result.succ(college);
    }

}
