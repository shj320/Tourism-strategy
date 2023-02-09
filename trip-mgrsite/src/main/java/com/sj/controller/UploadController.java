package com.sj.controller;


import com.sj.util.UploadUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadController {


    @RequestMapping("/uploadImg")
    @ResponseBody
    public String uploadImg(MultipartFile pic) throws Exception {

        //pic.getInputStream() 图片流，保存即可
        //web 阶段--webapp/uploads
        //Springboot 阶段--配置静态映射路径执行某个硬盘文件夹
        //前后端分离项目阶段---文件共享服务器

        String path = UploadUtil.uploadAli(pic);


        return path;
    }

    @RequestMapping("/uploadImg_ck")
    @ResponseBody
    public Map<String, Object> uploadImg_ck(MultipartFile upload) {

        Map<String, Object> map = new HashMap<>();

        if(upload != null && upload.getSize() > 0){
            try {
                String path =  UploadUtil.uploadAli(upload);
                map.put("uploaded", 1);
                map.put("url", path);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("uploaded", 0);
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("message", e.getMessage());
                map.put("error", errorMap);
            }
        }

        System.out.println(map);
        return map;
    }
}
