package com.example.flowbase_backend.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Service
public class GitService {

    /**
     * Clone a Git repository to a local directory
     * 
     * @param repositoryUrl The URL of the Git repository to clone
     * @param localPath The local path where the repository will be cloned
     * @param username Optional username for authentication (can be null)
     * @param password Optional password for authentication (can be null)
     * @return The Git instance for the cloned repository
     * @throws GitAPIException If a Git-related error occurs
     */
    public Git cloneRepository(String repositoryUrl, String localPath, String username, String password) throws GitAPIException {
        // Create credentials provider if username and password are provided
        UsernamePasswordCredentialsProvider credentialsProvider = null;
        if (username != null && password != null) {
            credentialsProvider = new UsernamePasswordCredentialsProvider(username, password);
        }

        // Clone the repository
        Git git;
        if (credentialsProvider != null) {
            git = Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(new File(localPath))
                    .setCredentialsProvider(credentialsProvider)
                    .call();
        } else {
            git = Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(new File(localPath))
                    .call();
        }
        
        return git;
    }

    /**
     * Pull changes from a remote repository
     * 
     * @param localPath The local path of the Git repository
     * @param username Optional username for authentication (can be null)
     * @param password Optional password for authentication (can be null)
     * @throws GitAPIException If a Git-related error occurs
     * @throws IOException If an I/O error occurs
     */
    public void pullChanges(String localPath, String username, String password) throws GitAPIException, IOException {
        // Create credentials provider if username and password are provided
        UsernamePasswordCredentialsProvider credentialsProvider = null;
        if (username != null && password != null) {
            credentialsProvider = new UsernamePasswordCredentialsProvider(username, password);
        }

        // Open the repository and pull changes
        try (Git git = Git.open(new File(localPath))) {
            if (credentialsProvider != null) {
                git.pull()
                   .setCredentialsProvider(credentialsProvider)
                   .call();
            } else {
                git.pull().call();
            }
        }
    }

    /**
     * Commit changes to a local repository
     * 
     * @param localPath The local path of the Git repository
     * @param message The commit message
     * @param authorName The name of the author
     * @param authorEmail The email of the author
     * @throws GitAPIException If a Git-related error occurs
     * @throws IOException If an I/O error occurs
     */
    public void commitChanges(String localPath, String message, String authorName, String authorEmail) throws GitAPIException, IOException {
        try (Git git = Git.open(new File(localPath))) {
            // Add all changes to the index
            git.add().addFilepattern(".").call();
            
            // Commit the changes
            git.commit()
               .setMessage(message)
               .setAuthor(authorName, authorEmail)
               .call();
        }
    }

    /**
     * Push changes to a remote repository
     * 
     * @param localPath The local path of the Git repository
     * @param username Username for authentication
     * @param password Password for authentication
     * @throws GitAPIException If a Git-related error occurs
     * @throws IOException If an I/O error occurs
     */
    public void pushChanges(String localPath, String username, String password) throws GitAPIException, IOException {
        // Create credentials provider
        UsernamePasswordCredentialsProvider credentialsProvider = 
            new UsernamePasswordCredentialsProvider(username, password);

        // Open the repository and push changes
        try (Git git = Git.open(new File(localPath))) {
            git.push()
               .setCredentialsProvider(credentialsProvider)
               .call();
        }
    }

    /**
     * Delete a local repository
     * 
     * @param localPath The local path of the Git repository
     * @throws IOException If an I/O error occurs
     */
    public void deleteRepository(String localPath) throws IOException {
        Path path = Path.of(localPath);
        if (Files.exists(path)) {
            Files.walk(path)
                 .sorted(Comparator.reverseOrder())
                 .map(Path::toFile)
                 .forEach(File::delete);
        }
    }
}