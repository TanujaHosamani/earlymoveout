package com.projectx.earlymoveout.repository;

import com.projectx.earlymoveout.entity.FacultyRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FacultyRequestRepository extends JpaRepository<FacultyRequest, Long> {

    // Pending list for HOD
    List<FacultyRequest> findByStatusOrderByIdDesc(String status);

    // Approved/Rejected date-wise history for HOD
    List<FacultyRequest> findByStatusAndDecisionDateTimeBetweenOrderByDecisionDateTimeDesc(
            String status, LocalDateTime start, LocalDateTime end
    );

    // ✅ Faculty should see only their requests
    List<FacultyRequest> findByCreatedByOrderByIdAsc(String createdBy);
}
