package com.example.flowbase_backend.controller;

import com.example.flowbase_backend.model.GitRepositoryRequest;
import com.example.flowbase_backend.model.GitRepositoryResponse;
import com.example.flowbase_backend.service.GitService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/git")
public class GitController {

    private final GitService gitService;

    @Autowired
    public GitController(GitService gitService) {
        this.gitService = gitService;
    }

    @PostMapping("/clone")
    public ResponseEntity<GitRepositoryResponse> cloneRepository(@RequestBody GitRepositoryRequest request) {

        try {
            Git git = gitService.cloneRepository(
                request.getRepositoryUrl(), 
                request.getLocalPath(), 
                request.getUsername(), 
                request.getPassword()
            );
            git.close(); // Close the Git instance after use

            GitRepositoryResponse response = GitRepositoryResponse.success("Repository cloned successfully")
                .addData("localPath", request.getLocalPath());

            return ResponseEntity.ok(response);
        } catch (GitAPIException e) {
            GitRepositoryResponse errorResponse = GitRepositoryResponse.error("Failed to clone repository: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/pull")
    public ResponseEntity<GitRepositoryResponse> pullChanges(@RequestBody GitRepositoryRequest request) {

        try {
            gitService.pullChanges(
                request.getLocalPath(), 
                request.getUsername(), 
                request.getPassword()
            );

            GitRepositoryResponse response = GitRepositoryResponse.success("Changes pulled successfully");

            return ResponseEntity.ok(response);
        } catch (GitAPIException | IOException e) {
            GitRepositoryResponse errorResponse = GitRepositoryResponse.error("Failed to pull changes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/commit")
    public ResponseEntity<GitRepositoryResponse> commitChanges(@RequestBody GitRepositoryRequest request) {

        try {
            gitService.commitChanges(
                request.getLocalPath(), 
                request.getMessage(), 
                request.getAuthorName(), 
                request.getAuthorEmail()
            );

            GitRepositoryResponse response = GitRepositoryResponse.success("Changes committed successfully");

            return ResponseEntity.ok(response);
        } catch (GitAPIException | IOException e) {
            GitRepositoryResponse errorResponse = GitRepositoryResponse.error("Failed to commit changes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/push")
    public ResponseEntity<GitRepositoryResponse> pushChanges(@RequestBody GitRepositoryRequest request) {

        try {
            gitService.pushChanges(
                request.getLocalPath(), 
                request.getUsername(), 
                request.getPassword()
            );

            GitRepositoryResponse response = GitRepositoryResponse.success("Changes pushed successfully");

            return ResponseEntity.ok(response);
        } catch (GitAPIException | IOException e) {
            GitRepositoryResponse errorResponse = GitRepositoryResponse.error("Failed to push changes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/repository")
    public ResponseEntity<GitRepositoryResponse> deleteRepository(@RequestBody GitRepositoryRequest request) {
        try {
            gitService.deleteRepository(request.getLocalPath());

            GitRepositoryResponse response = GitRepositoryResponse.success("Repository deleted successfully");

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            GitRepositoryResponse errorResponse = GitRepositoryResponse.error("Failed to delete repository: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
