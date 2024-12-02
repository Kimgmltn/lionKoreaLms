package kr.co.lionkorea.controller;

import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.dto.request.*;
import kr.co.lionkorea.dto.response.*;
import kr.co.lionkorea.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectRestController {

    private final ProjectService projectService;

    @PostMapping("/admin/save")
    public ResponseEntity<SaveProjectResponse> saveProject(@RequestBody SaveProjectRequest request) {
        return ResponseEntity.ok(projectService.saveProject(request));
    }

    @GetMapping("/admin/{projectId}")
    public ResponseEntity<FindProjectDetailForAdminResponse> findProjectForAdminById(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(projectService.findProjectDetailForAdmin(projectId));
    }

    @PatchMapping("/admin/{projectId}/reject")
    public ResponseEntity<SaveRejectProjectResponse> rejectProject(@PathVariable Long projectId, @RequestBody SaveRejectProjectRequest request){
        return ResponseEntity.ok(projectService.rejectProject(projectId, request));
    }

    @GetMapping("/admin")
    public ResponseEntity<PagedModel<FindProjectsForAdminResponse>> findProjectsForAdmin(@ModelAttribute FindProjectsForAdminRequest request, Pageable pageable) {
        return ResponseEntity.ok(projectService.findProjectsForAdmin(request, pageable));
    }

    @GetMapping("/translator")
    public ResponseEntity<PagedModel<FindProjectsForTranslatorResponse>> findProjectsForTranslator(@ModelAttribute FindProjectsForTranslatorRequest request, Pageable pageable, @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(projectService.findProjectsForTranslator(request, pageable, userDetails));
    }

    @GetMapping("/translator/{projectId}")
    public ResponseEntity<FindProjectDetailForTranslatorResponse> findProjectForTranslatorById(@PathVariable Long projectId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(projectService.findProjectDetailForTranslator(projectId, userDetails));
    }

    @PatchMapping("/translator/{projectId}/complete")
    public ResponseEntity<SaveCompleteConsultationResponse> completeConsultation(@PathVariable Long projectId, @RequestBody SaveCompleteConsultationRequest request) {
        return ResponseEntity.ok(projectService.completeConsultation(projectId, request));
    }

    @PatchMapping("/translator/{projectId}/start")
    public ResponseEntity<Void> startConsultation(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.startConsultation(projectId));
    }
}