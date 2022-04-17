package com.noodles.healthycode.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.noodles.healthycode.common.lang.Result;
import com.noodles.healthycode.entity.*;
import com.noodles.healthycode.service.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentService studentService;

    @Autowired
    CollegeService collegeService;

    @Autowired
    MajorService majorService;

    @Autowired
    IoapplyService ioapplyService;

    @Autowired
    BackapplyService backapplyService;

    @Autowired
    HealthyinfoService healthyinfoService;

    @PostMapping("/id")
    public Result getInfo(@RequestBody Student student) {

        Student student1= studentService.getOne(new QueryWrapper<Student>().eq("id", student.getId()));
        Major m = majorService.getOne(new QueryWrapper<Major>().eq("id", student1.getMajor()));
        College c= collegeService.getOne(new QueryWrapper<College>().eq("id", m.getCollege()));
       
        return Result.succ(MapUtil.builder()
                .put("name",student1.getName())
                .put("idcard",student1.getIdcard())
                .put("id",student1.getId())
                .put("college",c.getName())
                .put("color",student1.getHealthycode())
                .map());
    }

    @PostMapping("/changepw")
    public Result changepw(@RequestBody Map<String,String>m){

        Student student = studentService.getOne(new QueryWrapper<Student>().eq("id",m.get("id")));
        if(m.get("oldpassword").equals(student.getPassword())){
            student.setPassword(m.get("newpassword"));
            studentService.updateById(student);
            return Result.succ(1);
        }else {
            return Result.fail("密码错误");
        }
    }

    @PostMapping("/isdaily")
    public Result isdaily(@RequestBody Map<String,String>m){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String current = simpleDateFormat.format(new Date());

        Healthyinfo healthyinfo = healthyinfoService.getOne(new QueryWrapper<Healthyinfo>().and(i->i.eq("owner",m.get("id")).eq("time",current)));
        if(healthyinfo == null){
            return Result.succ(null);
        }else {
            return Result.fail(null);
        }
    }

    @PostMapping("/isapply")
    public Result isapply(@RequestBody Map<String,String>m){
        Backapply backapply = backapplyService.getOne(new QueryWrapper<Backapply>().and(i->i.eq("owner",m.get("id")).eq("teach","0")));
        Ioapply ioapply = ioapplyService.getOne(new QueryWrapper<Ioapply>().and(i->i.eq("owner",m.get("id")).eq("teach","0")));

        if(backapply == null && ioapply == null){
            return Result.succ(null);
        }else {
            return Result.fail(null);
        }
    }

    @PostMapping("/apply")
    public Result apply(@RequestBody Map<String,String> m){

        Backapply backapply = backapplyService.getOne(new QueryWrapper<Backapply>().and(i -> i.eq("owner",m.get("id"))
                .eq("state","0").eq("teach","1")));
        Ioapply ioapply = ioapplyService.getOne(new QueryWrapper<Ioapply>().and(i -> i.eq("owner",m.get("id"))
                .eq("state","0").eq("teach","1")));

        if(backapply != null){
            backapply.setState("1");
            backapplyService.updateById(backapply);
            return Result.succ(MapUtil.builder().put("t","返校申请").put("pass",backapply.getPass()).map());
        }

        if(ioapply != null){
            ioapply.setState("1");
            ioapplyService.updateById(ioapply);
            return Result.succ(MapUtil.builder().put("t","出入校申请").put("pass",ioapply.getPass()).map());
        }

        return Result.succ(null);

    }


    @PostMapping("/addstudent")
    public Result addstudent(@RequestBody MultipartFile file) throws IOException {

        Student student = new Student();

        BufferedInputStream in = new BufferedInputStream(file.getInputStream());
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);

        for(int sheetIndex = 0;sheetIndex < xssfWorkbook.getNumberOfSheets();sheetIndex++){
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetIndex);

            for(int rowIndex = 1;rowIndex <= xssfSheet.getLastRowNum();rowIndex++){

                XSSFRow xssfRow = xssfSheet.getRow(rowIndex);

                Major major = majorService.getOne(new QueryWrapper<Major>().eq("name",xssfRow.getCell(4).getStringCellValue()+"系"));

                student.setId(xssfRow.getCell(0).getStringCellValue());
                student.setIdcard(xssfRow.getCell(1).getStringCellValue());
                student.setName(xssfRow.getCell(2).getStringCellValue());
                student.setMajor(major.getId());

                if(!studentService.save(student)){
                    return Result.fail(null);
                }
            }
        }

        return Result.succ(null);
    }



}
