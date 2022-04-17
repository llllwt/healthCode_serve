package com.noodles.healthycode.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.noodles.healthycode.common.lang.Result;
import com.noodles.healthycode.entity.*;
import com.noodles.healthycode.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @Autowired
    BackapplyService backapplyService;

    @Autowired
    IoapplyService ioapplyService;

    @Autowired
    StudentService studentService;

    @Autowired
    MajorService majorService;

    @Autowired
    CollegeService collegeService;


    @PostMapping("/changepw")
    public Result changepw(@RequestBody Map<String,String> m){

        Manager manager = managerService.getOne(new QueryWrapper<Manager>().eq("id",m.get("id")));
        if(m.get("oldpassword").equals(manager.getPassword())){
            manager.setPassword(m.get("newpassword"));
            managerService.updateById(manager);
            return Result.succ(null);
        }else {
            return Result.fail("密码错误");
        }
    }

    @PostMapping("/check")
    public Result check(@RequestBody Map<String,String> m){
          List ba =  backApply(m.get("id"));

          List ia = ioApply(m.get("id"));

          return Result.succ(MapUtil.builder().put("back",ba).put("io",ia).map());

    }

    @PostMapping("/lookio")
    public Result lookio(@RequestBody Map<String,String>m) throws IOException {

        Ioapply ioapply = ioapplyService.getOne(new QueryWrapper<Ioapply>().eq("oid",m.get("id")));
        String path = ioapply.getAddress();

        List<String> ls = new ArrayList<>();
        String thisline = null;

        File f = new File(path);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        while ((thisline = bufferedReader.readLine())!=null){
            ls.add(thisline);
        }

        return Result.succ(ls);
    }

    @PostMapping("/lookback")
    public Result lookback(@RequestBody Map<String,String>m) throws IOException {

        Backapply backapply = backapplyService.getOne(new QueryWrapper<Backapply>().eq("aid",m.get("id")));
        String path = backapply.getAddress();

        List<String> ls = new ArrayList<>();
        String thisline = null;

        File f = new File(path);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        while ((thisline = bufferedReader.readLine())!=null){
            ls.add(thisline);
        }

        return Result.succ(ls);
    }

    @PostMapping("/dealio")
    public Result dealio(@RequestBody Map<String,String>m){

        Ioapply ioapply = ioapplyService.getOne(new QueryWrapper<Ioapply>().eq("oid",m.get("id")));

        ioapply.setPass(m.get("pass"));
        ioapply.setTeach("1");

        ioapplyService.updateById(ioapply);

        return Result.succ(null);
    }

    @PostMapping("/dealback")
    public Result dealback(@RequestBody Map<String,String>m){

        Backapply backapply = backapplyService.getOne(new QueryWrapper<Backapply>().eq("aid",m.get("id")));

        backapply.setPass(m.get("pass"));
        backapply.setTeach("1");

        backapplyService.updateById(backapply);

        return Result.succ(null);
    }

    private List backApply(String id){
        List<Map<String,String>> students = new ArrayList<>();
        Manager admin = managerService.getOne(new QueryWrapper<Manager>().eq("id",id));
        List<Backapply> backapplyList = null;

        if(admin.getRoot() == 3 || admin.getRoot() == 2){
            backapplyList= backapplyService.list(new QueryWrapper<Backapply>().eq("teach","0"));
            if(!backapplyList.isEmpty()){

                for (Backapply backapply : backapplyList) {
                    Map<String, String> map = new HashMap<>();
                    Student student = studentService.getOne(new QueryWrapper<Student>().eq("id", backapply.getOwner()));
                    map.put("id", student.getId());
                    map.put("name", student.getName());

                    Major major = majorService.getOne(new QueryWrapper<Major>().eq("id", student.getMajor()));
                    College college = collegeService.getOne(new QueryWrapper<College>().eq("id", major.getCollege()));

                    map.put("college", college.getName());
                    map.put("major", major.getName());
                    map.put("rowid",backapply.getAid());

                    students.add(map);
                }
                return students;
            }

        }else {
            List<Major> majors = majorService.list(new QueryWrapper<Major>().eq("college",admin.getCollege()));
            List<Integer> mid = new ArrayList<>();
            for (Major major : majors) mid.add(major.getId());
            List<Student> s = studentService.list(new QueryWrapper<Student>().in("major",mid));
            List<String> sid = new ArrayList<>();
            for (Student student : s) sid.add(student.getId());

            backapplyList = backapplyService.list(new QueryWrapper<Backapply>().and(i -> i.eq("teach","0").in("owner",sid)));
            if(!backapplyList.isEmpty()){

                for (Backapply backapply : backapplyList) {
                    Map<String, String> map = new HashMap<>();
                    Student student = studentService.getOne(new QueryWrapper<Student>().eq("id", backapply.getOwner()));
                    map.put("id", student.getId());
                    map.put("name", student.getName());

                    Major major = majorService.getOne(new QueryWrapper<Major>().eq("id", student.getMajor()));
                    College college = collegeService.getOne(new QueryWrapper<College>().eq("id", major.getCollege()));

                    map.put("college", college.getName());
                    map.put("major", major.getName());
                    map.put("rowid",backapply.getAid());

                    students.add(map);
                }
                return students;
            }
        }
        return null;
    }

    private List ioApply(String id){
        List<Map<String,String>> students = new ArrayList<>();
        Manager admin = managerService.getOne(new QueryWrapper<Manager>().eq("id",id));
        List<Ioapply> ioapplyList = null;

        if(admin.getRoot() == 3 || admin.getRoot() == 2){
            ioapplyList= ioapplyService.list(new QueryWrapper<Ioapply>().eq("teach","0"));
            if(!ioapplyList.isEmpty()){

                for (Ioapply ioapply : ioapplyList) {
                    Map<String, String> map = new HashMap<>();
                    Student student = studentService.getOne(new QueryWrapper<Student>().eq("id", ioapply.getOwner()));
                    map.put("id", student.getId());
                    map.put("name", student.getName());

                    Major major = majorService.getOne(new QueryWrapper<Major>().eq("id", student.getMajor()));
                    College college = collegeService.getOne(new QueryWrapper<College>().eq("id", major.getCollege()));

                    map.put("college", college.getName());
                    map.put("major", major.getName());
                    map.put("rowid",ioapply.getOid());

                    students.add(map);
                }
                return students;
            }

        }else {
            List<Major> majors = majorService.list(new QueryWrapper<Major>().eq("college",admin.getCollege()));
            List<Integer> mid = new ArrayList<>();
            for (Major major : majors) mid.add(major.getId());
            List<Student> s = studentService.list(new QueryWrapper<Student>().in("major",mid));
            List<String> sid = new ArrayList<>();
            for (Student student : s) sid.add(student.getId());

            ioapplyList = ioapplyService.list(new QueryWrapper<Ioapply>().and(i -> i.eq("teach","0").in("owner",sid)));
            if(!ioapplyList.isEmpty()){

                for (Ioapply ioapply : ioapplyList) {
                    Map<String, String> map = new HashMap<>();
                    Student student = studentService.getOne(new QueryWrapper<Student>().eq("id", ioapply.getOwner()));
                    map.put("id", student.getId());
                    map.put("name", student.getName());

                    Major major = majorService.getOne(new QueryWrapper<Major>().eq("id", student.getMajor()));
                    College college = collegeService.getOne(new QueryWrapper<College>().eq("id", major.getCollege()));

                    map.put("college", college.getName());
                    map.put("major", major.getName());
                    map.put("rowid",ioapply.getOid());

                    students.add(map);
                }
                return students;
            }
        }
        return null;
    }


}
