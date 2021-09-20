# Gitlet Design Document

**Name**: Bolong Zheng

## Classes and Data Structures

### Class 1: Main

#### Description

1. Creating a repository object
2. Calling different commands on the repository object
3. Edge case and some error handling

### Class 2: Repository

#### Description: Most of the codes live here

1. Static final variables:
   CWD, GITLET_DIR, STAGE, BLOB
2. Static variables:
    addStage, removeStage, commits, branch(not yet implemented)
   , HEAD, blobHub, commistLL (linked list)

Important notes:
1. commits is a hashmap; the structure is as follows:
- Key: Sha1 of a commit object.
- value: parent - Sha1 of the parent object), 
         files - reference to files in BLOB

2. Instance varibales of individual commits:
- timeStamp, message, Sha1, blobsHub, parent

3. Dealing with blob
- Blob is an area for all versions of files
- When making a commit, will add new files if 
the version is not in blob; if the version is alr there,
will just point to that version and alter the instance
  variable of the current commit to these versions.

## Algorithms

## Persistence
init will 
