package com.projectx.earlymoveout.controller;

import com.projectx.earlymoveout.entity.FacultyRequest;
import com.projectx.earlymoveout.repository.FacultyRequestRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/faculty")
@CrossOrigin(origins = "*")
public class FacultyRequestController {

    private final FacultyRequestRepository repo;

    public FacultyRequestController(FacultyRequestRepository repo) {
        this.repo = repo;
    }

    // ✅ CREATE request (save which user created it)
    @PostMapping("/request")
    public FacultyRequest create(@RequestBody FacultyRequest request, Authentication auth) {

        if (request.getStatus() == null) request.setStatus("PENDING");
        if (request.getRequestDate() == null) request.setRequestDate(LocalDate.now());
        if (request.getRequestTime() == null) request.setRequestTime(LocalTime.now());

        // IMPORTANT: store logged-in username
        request.setCreatedBy(auth.getName());

        return repo.save(request);
    }

    // ✅ LIST only MY requests (not others)
    @GetMapping("/my-requests")
    public List<FacultyRequest> myRequests(Authentication auth) {
        return repo.findByCreatedByOrderByIdAsc(auth.getName());
    }
}
