package by.betterremm.InformationTheory.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/lab2")
public class Lab2Controller {

    @GetMapping("/register")
    public static String register(){
        return "lab2/register";
    }

    @PostMapping("/register")
    public static String registerInitialize(
            @RequestParam("register_state") String registerState
    ){

        //Нормализировать начальный регистр
        return "redirect:/lab2/LFSR/" + registerState;
    }

    @GetMapping("/LFSR/{register}")
    public static String LFSR(@PathVariable("register") String register,
                              Model model,
                              HttpSession session
    ){
        //Вывести результат если это редирект и есть что выводить
        session.setAttribute("result", "");
        model.addAttribute("register", register);
        return "lab2/LFSR";
    }

    @PostMapping("/LFSR/{register}")
    public static String LFSRPost(
            @PathVariable("register") String register,
            @RequestParam("action") String action,
            @RequestParam("text") String text,
            @RequestParam("file") MultipartFile file,
            Model model,
            HttpSession session
    ) {
        String result = "";

        //зашифровать расшифровать

        session.setAttribute("result", result);
        return "redirect:/lab2/LFSR/" + register;
    }


}
