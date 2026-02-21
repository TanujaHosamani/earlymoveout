package com.projectx.earlymoveout.controler;

import com.projectx.earlymoveout.entity.UserAccount;
import com.projectx.earlymoveout.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role,
            @RequestParam(required = false) String facultyId,
            RedirectAttributes ra
    ) {
        String cleanUsername = (username == null) ? "" : username.trim();

        if (cleanUsername.isEmpty() || password == null || password.isEmpty() || role == null || role.isEmpty()) {
            ra.addAttribute("msg", "Fill all fields");
            return "redirect:/register.html";
        }

        if (userAccountRepository.findByUsernameIgnoreCase(cleanUsername).isPresent()) {
            ra.addAttribute("msg", "Username already exists");
            return "redirect:/register.html";
        }

        UserAccount user = new UserAccount();
        user.setUsername(cleanUsername);
        user.setPassword(passwordEncoder.encode(password)); // ✅ BCrypt
        user.setRole(role.trim().toUpperCase());            // FACULTY/HOD/GATE
        user.setFacultyId(facultyId);

        userAccountRepository.save(user);

        ra.addAttribute("created", "true");
        return "redirect:/faculty-login.html";
    }
}
