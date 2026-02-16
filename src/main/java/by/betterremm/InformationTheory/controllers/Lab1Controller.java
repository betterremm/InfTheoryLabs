package by.betterremm.InformationTheory.controllers;


import by.betterremm.InformationTheory.services.PlayfairService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/lab1")
public class Lab1Controller {

    public Lab1Controller(SpringResourceTemplateResolver springResourceTemplateResolver) {
    }

    @GetMapping("")
    public String nav(Model model) {
        return "lab1/nav";
    }

    @GetMapping("/playfair")
    public String playfair() {
        return "lab1/playfair";
    }

    @PostMapping("/playfair")
    public String playfairPost(
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name = "action", required = false) String action,
            Model model,
            HttpSession session) {
        String inputData;
        System.out.println(file == null ? "null" : file.getOriginalFilename());
        if (file != null && !file.isEmpty()) {
            try {
                inputData = new String(file.getBytes());
            }catch (IOException e){
                model.addAttribute("error", "File Error");
                return "lab1/playfair";
            }
        }
        else if (text != null) {
            inputData = text;
        }
        else {
            return "lab1/playfair";
        }

        if (action.equals("Зашифровать")) {
            model.addAttribute("result", PlayfairService.encrypt(key, inputData));
            session.setAttribute("playfair", PlayfairService.encrypt(key, inputData));
        }
        else {
            model.addAttribute("result", PlayfairService.decrypt(key, inputData));
            session.setAttribute("playfair", PlayfairService.decrypt(key, inputData));
        }

        return "lab1/playfair";
    }

    @GetMapping("/playfair/download")
    public ResponseEntity<byte[]> downloadFile(HttpSession session) {

        byte[] data = String.valueOf(session.getAttribute("playfair")).getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"playfair.txt\"")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(data.length)
                .body(data);
    }





}
