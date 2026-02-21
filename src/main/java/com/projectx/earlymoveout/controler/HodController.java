package com.projectx.earlymoveout.controler;

import com.projectx.earlymoveout.entity.FacultyRequest;
import com.projectx.earlymoveout.repository.FacultyRequestRepository;
import com.projectx.earlymoveout.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hod")
public class HodController {

    @Autowired
    private FacultyRequestRepository facultyRequestRepository;

    @Autowired
    private OtpService otpService;

    // ✅ 1) Load pending requests
    @GetMapping("/pending")
    @ResponseBody
    public List<FacultyRequest> getPendingRequests() {
        return facultyRequestRepository.findByStatusOrderByIdDesc("PENDING");
    }

    // ✅ 2) Load approved list by date (YYYY-MM-DD)
    @GetMapping("/approved")
    @ResponseBody
    public List<FacultyRequest> getApprovedByDate(@RequestParam String date) {
        LocalDate d = LocalDate.parse(date);
        LocalDateTime start = d.atStartOfDay();
        LocalDateTime end = d.plusDays(1).atStartOfDay();

        return facultyRequestRepository
                .findByStatusAndDecisionDateTimeBetweenOrderByDecisionDateTimeDesc("APPROVED", start, end);
    }

    // ✅ 3) Load rejected list by date (YYYY-MM-DD)
    @GetMapping("/rejected")
    @ResponseBody
    public List<FacultyRequest> getRejectedByDate(@RequestParam String date) {
        LocalDate d = LocalDate.parse(date);
        LocalDateTime start = d.atStartOfDay();
        LocalDateTime end = d.plusDays(1).atStartOfDay();

        return facultyRequestRepository
                .findByStatusAndDecisionDateTimeBetweenOrderByDecisionDateTimeDesc("REJECTED", start, end);
    }

    // ✅ 4) Approve request => Generates random OTP
    @PostMapping("/approve")
    @ResponseBody
    public ResponseEntity<?> approveRequest(@RequestParam Long requestId) {

        FacultyRequest req = facultyRequestRepository.findById(requestId).orElse(null);
        if (req == null) {
            return ResponseEntity.status(404).body(Map.of("ok", false, "message", "Request not found"));
        }

        req.setStatus("APPROVED");
        req.setDecisionDateTime(LocalDateTime.now());
        req.setOtp(otpService.generate5DigitOtp());

        facultyRequestRepository.save(req);

        return ResponseEntity.ok(Map.of(
                "ok", true,
                "message", "Approved ✅",
                "otp", req.getOtp()
        ));
    }

    // ✅ 5) Reject request
    @PostMapping("/reject")
    @ResponseBody
    public ResponseEntity<?> rejectRequest(@RequestParam Long requestId) {

        FacultyRequest req = facultyRequestRepository.findById(requestId).orElse(null);
        if (req == null) {
            return ResponseEntity.status(404).body(Map.of("ok", false, "message", "Request not found"));
        }

        req.setStatus("REJECTED");
        req.setDecisionDateTime(LocalDateTime.now());
        req.setOtp(null);

        facultyRequestRepository.save(req);

        return ResponseEntity.ok(Map.of("ok", true, "message", "Rejected ❌"));
    }
}
