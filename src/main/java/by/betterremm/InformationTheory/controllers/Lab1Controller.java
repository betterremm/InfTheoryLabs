package by.betterremm.InformationTheory.controllers;


import by.betterremm.InformationTheory.services.CipherService;
import by.betterremm.InformationTheory.services.PlayfairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/lab1")
public class Lab1Controller {

    private final Map<String, CipherService> cipherServices;

    public Lab1Controller(SpringResourceTemplateResolver springResourceTemplateResolver, Map<String, CipherService> cipherServices) {
        this.cipherServices = cipherServices;
    }

    @GetMapping("")
    public String nav(Model model) {
        return "lab1/nav";
    }

    @GetMapping("/{cipher}")
    public String playfair(Model model, HttpSession session,
                           @PathVariable("cipher") String cipher) {

        Object result = session.getAttribute(cipher);
        Object error = session.getAttribute("error");

        model.addAttribute("result", result);
        model.addAttribute("error", error);

        session.removeAttribute("error");

        return "lab1/" + cipher;
    }


    @PostMapping("/{cipher}")
    public String playfairPost(
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name = "action", required = false) String action,
            @PathVariable("cipher") String cipher,
            Model model,
            HttpSession session) {
        String inputData;
        if (file != null && !file.isEmpty()) {
            try {
                inputData = new String(file.getBytes());
            }catch (IOException e){
                model.addAttribute("error", "File Error");
                return "redirect:/lab1/" + cipher;
            }
        }
        else if (text != null) {
            inputData = text;
        }
        else {
            return "redirect:/lab1/" + cipher;
        }


        if (action.equals("Зашифровать")) {
            String result = cipherServices.get(cipher).encrypt(key, inputData);
            model.addAttribute("result", result);
            session.setAttribute(action, "encrypt");
            session.setAttribute(cipher, result);
        }
        else {
            String result = cipherServices.get(cipher).decrypt(key, inputData);
            model.addAttribute("result", result);
            session.setAttribute(action, "decrypt");
            session.setAttribute(cipher, result);
        }

        return "redirect:/lab1/" + cipher;
    }

    @GetMapping("/{cipher}/download")
    public ResponseEntity<byte[]> downloadFile(
            HttpSession session,
            @PathVariable("cipher") String cipher) {
        byte[] data = String.valueOf(session.getAttribute(cipher)).getBytes();
        session.removeAttribute(cipher);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + cipher +
                                (session.getAttribute("action").equals("decrypt")? "_decrypted" : "_encrypted")
                                + ".txt\"")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(data.length)
                .body(data);

    }





}
