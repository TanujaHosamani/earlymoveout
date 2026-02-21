package com.projectx.earlymoveout.controler;

import com.projectx.earlymoveout.entity.FacultyRequest;
import com.projectx.earlymoveout.repository.FacultyRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/gate")
public class GateController {

    @Autowired
    private FacultyRequestRepository facultyRequestRepository;

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> body) {

        String requestIdStr = body.get("requestId");
        String otp = body.get("otp");

        if (requestIdStr == null || otp == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("ok", false, "message", "Missing requestId or otp"));
        }

        Long requestId;
        try {
            requestId = Long.valueOf(requestIdStr);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("ok", false, "message", "Request ID must be a number"));
        }

        FacultyRequest req = facultyRequestRepository.findById(requestId).orElse(null);
        if (req == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("ok", false, "message", "Request not found"));
        }

        if (!"APPROVED".equals(req.getStatus())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("ok", false, "message", "Request is not approved"));
        }

        if (req.getOtp() == null || !req.getOtp().equals(otp)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("ok", false, "message", "Invalid OTP"));
        }

        req.setStatus("EXITED");
        req.setExitDateTime(LocalDateTime.now());
        facultyRequestRepository.save(req);

        return ResponseEntity.ok(Map.of("ok", true, "message", "Verified ✅ Exit allowed"));
    }
}
