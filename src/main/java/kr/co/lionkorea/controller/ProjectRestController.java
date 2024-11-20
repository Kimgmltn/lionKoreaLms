package kr.co.lionkorea.controller;

import kr.co.lionkorea.dto.request.SaveProjectRequest;
import kr.co.lionkorea.dto.response.SaveProjectResponse;
import kr.co.lionkorea.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> findProjectForAdminById(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok().build();
    }
}
