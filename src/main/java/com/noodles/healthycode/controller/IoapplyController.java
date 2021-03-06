package com.noodles.healthycode.controller;

import com.noodles.healthycode.common.lang.Result;
import com.noodles.healthycode.entity.Ioapply;
import com.noodles.healthycode.service.IoapplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/ioapply")
public class IoapplyController {

    @Autowired
    IoapplyService ioapplyService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> m) throws IOException {

        Ioapply ioapply = new Ioapply();

        String temptime01 = m.get("sdate");
        String temptime02 = temptime01.substring(0,20);
        String[] temptime03 = temptime02.split("T");
        String[] temptime04 = temptime03[1].split(":");
        int hour = (Integer.parseInt(temptime04[0])+8);
        String stime = temptime03[0]+" "+hour+":"+temptime04[1]+":"+temptime04[2];

        String temptime11 = m.get("edate");
        String temptime12 = temptime11.substring(0,20);
        String[] temptime13 = temptime12.split("T");
        String[] temptime14 = temptime13[1].split(":");
        int hour0 = (Integer.parseInt(temptime14[0])+8);
        String etime = temptime13[0]+" "+hour0+":"+temptime14[1]+":"+temptime14[2];

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format((new Date()).getTime());
        System.out.println(time);

        ioapply.setOid(m.get("id")+time);
        ioapply.setOwner(m.get("id"));
        ioapply.setTime(time);
        ioapply.setState("0");
        ioapply.setTeach("0");

        String content = "??????: "+m.get("id")+"\n"+
                "??????: "+m.get("name")+"\n"+
                "????????????: "+m.get("idcard")+"\n"+
                "??????: "+m.get("college")+"\n"+
                "?????????: "+m.get("phone")+"\n"+
                "????????????: "+m.get("reason")+"\n"+
                "???????????????: "+m.get("address")+"\n"+
                "??????????????????: "+stime+"\n"+
                "??????????????????: "+etime+"\n";

        String path = "E:/??????/????????????/code/io_apply/"+m.get("id")+"_"+time+ ".txt";
        File f = new File(path);

        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(content);
        bw.close();

        ioapply.setAddress(path);
        ioapplyService.save(ioapply);

        return Result.succ(null);
    }

}
