package com.noodles.healthycode.controller;

import com.noodles.healthycode.common.lang.Result;
import com.noodles.healthycode.entity.Backapply;
import com.noodles.healthycode.service.BackapplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/backapply")
public class BackapplyController {

    @Autowired
    BackapplyService backapplyService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String>m) throws IOException {

        Backapply backapply = new Backapply();

        backapply.setState("0");

        String temptime1 = m.get("date");
        String temptime2 = temptime1.substring(0,20);
        String[] temptime3 = temptime2.split("T");
        String[] temptime4 = temptime3[1].split(":");
        int hour = (Integer.parseInt(temptime4[0])+8);
        String time = temptime3[0]+" "+hour+":"+temptime4[1]+":"+temptime4[2];

        backapply.setAid(m.get("id")+time);
        backapply.setTime(time);
        backapply.setOwner(m.get("id"));
        backapply.setState("0");
        backapply.setTeach("0");

        String content = "学号: "+m.get("id")+"\n"+
                "姓名: "+m.get("name")+"\n"+
                "身份证号: "+m.get("idcard")+"\n"+
                "学院: "+m.get("college")+"\n"+
                "手机号: "+m.get("phone")+"\n"+
                 "当前居住位置: "+m.get("location")+"\n"+
                "前一日体温: "+m.get("out")+"\n"+
                "本人近期（14 天内）是否去过中高风险地区: "+(m.get("midhigh").equals("1")?"是":"否")+"\n"+
                "本人近期（14 天内）是否去过国外？: "+(m.get("abroad").equals("1")?"是":"否")+"\n"+
                "本人近期（14 天内）是否接触过新冠确诊病人或疑似病人？: "+(m.get("touch").equals("1")?"是":"否")+"\n"+
                "本人是否被卫生部门确认为新冠肺炎确诊病例或疑似病例？: "+(m.get("confirm").equals("1")?"是":"否")+"\n"+
                "当前是否存在发烧（≥37.3℃）、乏力、干咳、鼻塞、流涕、咽痛、腹泻等症状。: "+(m.get("situation").equals("1")?"是":"否")+"\n"+
                "返校方式: "+m.get("tran")+"\n"+
                "预计返校时间： "+time;

        String path = "E:/大学/毕业设计/code/apply_back/"+m.get("id")+"_"+temptime3[0]+ ".txt";
        File f = new File(path);

        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(content);
        bw.close();

        backapply.setAddress(path);
        backapplyService.save(backapply);

        return Result.succ(null);

    }

}
