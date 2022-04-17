package com.noodles.healthycode.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.noodles.healthycode.common.lang.Result;
import com.noodles.healthycode.entity.*;
import com.noodles.healthycode.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/healthycode")
public class HealthycodeController {

    @Autowired
    HealthycodeService healthycodeService;

    @Autowired
    StudentService studentService;

    @Autowired
    ManagerService managerService;

    @Autowired
    CollegeService collegeService;

    @Autowired
    MajorService majorService;

    @PostMapping("/submit")
    public Result sub(@RequestBody Map<String,String> m)  {
        String color ;
        Student student1= studentService.getOne(new QueryWrapper<Student>().eq("id", m.get("id")));
        if(m.get("touch").equals("1")||m.get("confirm").equals("1")){
            color = "red";
        }else if(m.get("midhigh").equals("1")||m.get("abroad").equals("1")||m.get("situation").equals("1")){
            color = "yellow";
        }else {
            color = "green";
        }
        student1.setHealthycode(color);
        studentService.updateById(student1);
        return Result.succ(null);
    }

    private static String generateQRCodeImage(String color,String text, int width, int height) throws WriterException, IOException {

        int c = 0;
        int WHITE = 0xFFFFFFFF;
        switch (color){
            case "red":
                c = 0Xff0000;
                break;
            case "yellow":
                c = 0xffff00;
                break;
            case "green":
                c = 0x33ff00;
                break;
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        int w = bitMatrix.getWidth();
        int h = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                image.setRGB(x, y,bitMatrix.get(x, y) ? c : WHITE);
            }
        }


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image,"png",bos);
        bos.flush();
        byte[] src = bos.toByteArray();
        String ret= Base64.getEncoder().encodeToString(src);
        bos.close();

        return ret;
    }

    @PostMapping("/getcode")
    public Result get(@RequestBody Map<String,String>m){

        Student student1= studentService.getOne(new QueryWrapper<Student>().eq("id", m.get("id")));
        String src = "";
        try {
            src = generateQRCodeImage(student1.getHealthycode(),m.get("id")+"\n"+student1.getName(), 350, 350);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }


        return Result.succ(MapUtil.builder()
                .put("id",student1.getId())
                .put("name",student1.getName())
                .put("color",student1.getHealthycode())
                .put("src",src)
                .map());
    }

    @PostMapping("/admin")
    public Result admin(@RequestBody Map<String,String>m) throws ParseException {

        List<Map<String,String>> students = new ArrayList<>();
        Manager admin = managerService.getOne(new QueryWrapper<Manager>().eq("id",m.get("id")));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if(admin.getRoot() == 3 || admin.getRoot() == 2){
            List<Student> studentList = studentService.list();
            for(Student student : studentList) {
                Map<String, String> map = new HashMap<>();

                Major major = majorService.getOne(new QueryWrapper<Major>().eq("id", student.getMajor()));
                College college = collegeService.getOne(new QueryWrapper<College>().eq("id", major.getCollege()));

                Date time = simpleDateFormat.parse(student.getLasttime());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                long timeInMillis1 = calendar.getTimeInMillis();
                calendar.setTime(time);
                long timeInMillis2 = calendar.getTimeInMillis();
                String betweenDays = "" + ((timeInMillis1 - timeInMillis2) / (1000L * 3600L * 24L)) ;

                map.put("college", college.getName());
                map.put("major", major.getName());
                map.put("id", student.getId());
                map.put("name", student.getName());
                map.put("temp", betweenDays);
                map.put("color", student.getHealthycode());

                students.add(map);
            }
            return Result.succ(students);
        }else {
            List<Major> majors = majorService.list(new QueryWrapper<Major>().eq("college",admin.getCollege()));
            List<Integer> mid = new ArrayList<>();
            for (Major major : majors) mid.add(major.getId());
            List<Student> studentList = studentService.list(new QueryWrapper<Student>().in("major",mid));

            for(Student student : studentList) {
                Map<String, String> map = new HashMap<>();

                Major major = majorService.getOne(new QueryWrapper<Major>().eq("id", student.getMajor()));
                College college = collegeService.getOne(new QueryWrapper<College>().eq("id", major.getCollege()));

                Date time = simpleDateFormat.parse(student.getLasttime());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                long timeInMillis1 = calendar.getTimeInMillis();
                calendar.setTime(time);
                long timeInMillis2 = calendar.getTimeInMillis();
                String betweenDays = "" + ((timeInMillis1 - timeInMillis2) / (1000L * 3600L * 24L)) ;

                map.put("college", college.getName());
                map.put("major", major.getName());
                map.put("id", student.getId());
                map.put("name", student.getName());
                map.put("temp", betweenDays);
                map.put("color", student.getHealthycode());

                students.add(map);
            }
            return Result.succ(students);
        }

    }

    @PostMapping("/del")
    public Result del(@RequestBody Map<String,String>m){
        Student student = studentService.getOne(new QueryWrapper<Student>().eq("id",m.get("id")));
        student.setHealthycode("");
        studentService.updateById(student);

        return Result.succ(null);
    }


}
