package by.betterremm.InformationTheory.controllers;

import by.betterremm.InformationTheory.services.LFSRFileService;
import by.betterremm.InformationTheory.services.LFSRResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/lab2")
public class Lab2Controller {

    private final LFSRFileService lfsrService = new LFSRFileService();

    @GetMapping("")
    public String lab2Page(Model model, HttpSession session) {
        Object result = session.getAttribute("result");
        Object originalBinary = session.getAttribute("originalBinary");
        Object processedBinary = session.getAttribute("processedBinary");
        Object filename = session.getAttribute("filename");

        model.addAttribute("result", result);
        model.addAttribute("originalBinary", originalBinary);
        model.addAttribute("processedBinary", processedBinary);
        model.addAttribute("filename", filename);

        session.removeAttribute("result");
        session.removeAttribute("originalBinary");
        session.removeAttribute("processedBinary");
        session.removeAttribute("filename");

        return "lab2/lab2";
    }

    @PostMapping("")
    public String lab2Process(
            @RequestParam("register_state") String registerState,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("action") String action,
            Model model
    ) {
        try {
            StringBuilder msg = new StringBuilder();
            registerState = registerState.replaceAll("[^01]", ""); // только 0 и 1

            if (registerState.length() > 34) {
                registerState = registerState.substring(0, 34);
                msg.append("Регистр был обрезан до 34 бит.");
            } else if (registerState.length() < 34) {
                while (registerState.length() < 34) registerState += "0";
                msg.append("Регистр был дополнен нулями до 34 бит.");
            }

            byte[] inputBytes;
            if (file != null && !file.isEmpty()) {
                inputBytes = file.getBytes();
            } else if (text != null && !text.isEmpty()) {
                inputBytes = text.getBytes();
            } else {
                model.addAttribute("register_state", registerState);
                model.addAttribute("text", text);
                model.addAttribute("registerMessage", "Нет входных данных");
                return "lab2/lab2";
            }

            LFSRResult lfsrResult = lfsrService.processWithKey(inputBytes, registerState);
            byte[] outputBytes = lfsrResult.getOutputBytes();
            String lfsrKey = formatBinary(lfsrResult.getKeyBinary());

            String outputFileName = (file != null && !file.isEmpty() && file.getOriginalFilename() != null ? (file.getOriginalFilename().endsWith(".enc")? file.getOriginalFilename().substring(0, file.getOriginalFilename().length() - 4) : file.getOriginalFilename() + ".enc") : "text.txt");
            File outFile = new File(System.getProperty("user.dir") + "/" + outputFileName);
            try (FileOutputStream fos = new FileOutputStream(outFile)) {
                fos.write(outputBytes);
            }

            model.addAttribute("register_state", registerState);
            model.addAttribute("registerMessage", msg.toString());
            model.addAttribute("text", text);
            model.addAttribute("originalBinary", formatBinary(toBinaryString(inputBytes)));
            model.addAttribute("processedBinary", formatBinary(toBinaryString(outputBytes)));
            model.addAttribute("lfsrKey", lfsrKey);
            model.addAttribute("filename", outputFileName);

        } catch (IOException e) {
            model.addAttribute("register_state", registerState);
            model.addAttribute("text", text);
            model.addAttribute("registerMessage", "Ошибка: " + e.getMessage());
        }

        return "lab2/lab2";
    }


    private String formatBinary(String binary) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < binary.length(); i++) {
            sb.append(binary.charAt(i));
            if ((i + 1) % 8 == 0) sb.append(' ');
        }
        return sb.toString().trim();
    }

    private String toBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return sb.toString();
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("filename") String filename) throws IOException {
        File file = new File(System.getProperty("user.dir") + "/" + filename);
        byte[] data = java.nio.file.Files.readAllBytes(file.toPath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(data.length)
                .body(data);
    }

    private String normalizeRegister(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == '0' || c == '1') sb.append(c);
        }
        if (sb.length() > 34) sb.setLength(34);
        while (sb.length() < 34) sb.append('0');
        return sb.toString();
    }

}