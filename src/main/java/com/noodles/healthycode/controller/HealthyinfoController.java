package com.noodles.healthycode.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.noodles.healthycode.common.lang.Result;
import com.noodles.healthycode.entity.Healthyinfo;
import com.noodles.healthycode.entity.Student;
import com.noodles.healthycode.service.HealthycodeService;
import com.noodles.healthycode.service.HealthyinfoService;
import com.noodles.healthycode.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/healthyinfo")
public class HealthyinfoController {

    @Autowired
    HealthyinfoService healthyinfoService;

    @Autowired
    StudentService studentService;

    @PostMapping("/submit")
    public Result sub(@RequestBody Map<String,String>m) throws IOException, ParseException {

        Healthyinfo healthyinfo = new Healthyinfo();

        String string =  m.get("time").substring(0,10);
        String[] sss = string.split("-");
        String t = sss[0]+"-"+sss[1]+"-"+(Integer.parseInt(sss[2])+1);

        healthyinfo.setId(m.get("id")+t);

        healthyinfo.setOwner(m.get("id"));

        healthyinfo.setTime(t);

        String content = "学号: "+m.get("id")+"\n"+
                "姓名: "+m.get("name")+"\n"+
                "身份证号: "+m.get("idcard")+"\n"+
                "学院: "+m.get("college")+"\n"+
                "手机号: "+m.get("phone")+"\n"+
                "填报时间: "+t+"\n"+
                "当前状态: "+(m.get("state").equals("1")?"在校":"离校")+"\n";
        if(m.get("state").equals("1")){
            content += "前一日中午体温: "+m.get("in_noon")+"\n"+
                    "前一日晚体温: "+m.get("in_afternoon")+"\n"+
                    "当日早体温: "+m.get("in_morning")+"\n";
        }else {
            content += "当前居住位置: "+m.get("location")+"\n"+
                    "前一日体温: "+m.get("out")+"\n"+
                    "本人近期（14 天内）是否去过中高风险地区: "+(m.get("midhigh").equals("1")?"是":"否")+"\n"+
                    "本人近期（14 天内）是否去过国外？: "+(m.get("abroad").equals("1")?"是":"否")+"\n"+
                    "本人近期（14 天内）是否接触过新冠确诊病人或疑似病人？: "+(m.get("touch").equals("1")?"是":"否")+"\n"+
                    "本人是否被卫生部门确认为新冠肺炎确诊病例或疑似病例？: "+(m.get("confirm").equals("1")?"是":"否")+"\n"+
                    "当前是否存在发烧（≥37.3℃）、乏力、干咳、鼻塞、流涕、咽痛、腹泻等症状。: "+(m.get("situation").equals("1")?"是":"否")+"\n";
        }
        String path = "E:/大学/毕业设计/code/daily_health/"+m.get("id")+"_"+t+".txt";
        File f = new File(path);

        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(content);
        bw.close();


        healthyinfo.setAddress(path);
        healthyinfoService.save(healthyinfo);

        Student student= studentService.getOne(new QueryWrapper<Student>().eq("id", m.get("id")));
        String color = student.getHealthycode();
        if(m.get("touch").equals("1")||m.get("confirm").equals("1")){
            color = "red";
        }else if(m.get("midhigh").equals("1")||m.get("abroad").equals("1")||m.get("situation").equals("1")){
            color = "yellow";
        }
        student.setHealthycode(color);


        String time = student.getLasttime();
        int instance = student.getInstance();

        if(time != null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = simpleDateFormat.parse(time);
            Date date2 = simpleDateFormat.parse(t);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            long timeInMillis1 = calendar.getTimeInMillis();
            calendar.setTime(date2);
            long timeInMillis2 = calendar.getTimeInMillis();
            long betweenDays = ((timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L)) ;

            if(betweenDays == 1){
                instance++;
                student.setInstance(instance);
            }else {
                student.setInstance(1);
            }
        }


        if(student.getHealthycode().equals("yellow") && student.getInstance() >= 7 ){
            student.setHealthycode("green");
        }else if(student.getHealthycode() == "red" && student.getInstance() >= 14){
            student.setHealthycode("green");
        }
        student.setLasttime(t);

        studentService.updateById(student);

        return Result.succ(null);
    }

    @PostMapping("/history")
    public Result his(@RequestBody Map<String,String>m) throws IOException {

        List<Healthyinfo> l;
        List<String> time =new ArrayList<>();
        List<List> info = new ArrayList<>();

        l = healthyinfoService.list(new QueryWrapper<Healthyinfo>().eq("owner", m.get("id")));
        for(int i = 0;i<l.size();i++){
            time.add((l.get(i).getTime()));
            String path = l.get(i).getAddress();
            List<String> ls = new ArrayList<>();
            String thisline = null;

            File f = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            while ((thisline = bufferedReader.readLine())!=null){
                ls.add(thisline);
            }

            info.add(ls);

        }


        return Result.succ(MapUtil.builder()
                .put("time",time)
                .put("list",info)
                .map());


    }
}
