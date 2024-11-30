package kr.co.lionkorea.controller;

import kr.co.lionkorea.dto.request.FindProjectsForAdminRequest;
import kr.co.lionkorea.dto.request.SaveProjectRequest;
import kr.co.lionkorea.dto.request.SaveRejectProjectRequest;
import kr.co.lionkorea.dto.response.FindProjectDetailForAdminResponse;
import kr.co.lionkorea.dto.response.FindProjectsForAdminResponse;
import kr.co.lionkorea.dto.response.SaveProjectResponse;
import kr.co.lionkorea.dto.response.SaveRejectProjectResponse;
import kr.co.lionkorea.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
