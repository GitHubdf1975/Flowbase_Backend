# Git Integration for FlowBase Backend

This document describes the Git integration API for the FlowBase Backend application.

## Overview

The Git integration allows the application to interact with Git repositories through a REST API. The following operations are supported:

- Clone a Git repository
- Pull changes from a remote repository
- Commit changes to a local repository
- Push changes to a remote repository
- Delete a local repository

## API Endpoints

All Git operations are exposed through REST endpoints under the `/api/git` base path.

### Clone a Repository

**Endpoint:** `POST /api/git/clone`

**Request Body:**
```json
{
  "repositoryUrl": "https://github.com/username/repo.git",
  "localPath": "C:/path/to/local/repo",
  "username": "your-username",
  "password": "your-password"
}
```

Note: `username` and `password` fields are optional and can be omitted for public repositories.

**Response:**
```json
{
  "status": "success",
  "message": "Repository cloned successfully",
  "data": {
    "localPath": "C:/path/to/local/repo"
  }
}
```

### Pull Changes

**Endpoint:** `POST /api/git/pull`

**Request Body:**
```json
{
  "localPath": "C:/path/to/local/repo",
  "username": "your-username",
  "password": "your-password"
}
```

Note: `username` and `password` fields are optional and can be omitted for public repositories.

**Response:**
```json
{
  "status": "success",
  "message": "Changes pulled successfully",
  "data": {}
}
```

### Commit Changes

**Endpoint:** `POST /api/git/commit`

**Request Body:**
```json
{
  "localPath": "C:/path/to/local/repo",
  "message": "Commit message",
  "authorName": "Your Name",
  "authorEmail": "your.email@example.com"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Changes committed successfully",
  "data": {}
}
```

### Push Changes

**Endpoint:** `POST /api/git/push`

**Request Body:**
```json
{
  "localPath": "C:/path/to/local/repo",
  "username": "your-username",
  "password": "your-password"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Changes pushed successfully",
  "data": {}
}
```

### Delete Repository

**Endpoint:** `DELETE /api/git/repository`

**Request Body:**
```json
{
  "localPath": "C:/path/to/local/repo"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Repository deleted successfully",
  "data": {}
}
```

## Error Handling

All endpoints return appropriate HTTP status codes and error messages in case of failure:

```json
{
  "status": "error",
  "message": "Failed to clone repository: Invalid repository URL",
  "data": {}
}
```

## Security

The Git API endpoints are accessible without authentication. If you need to secure these endpoints, update the security configuration in `SecurityConfig.java`.
