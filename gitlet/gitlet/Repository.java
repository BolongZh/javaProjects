package gitlet;

import jdk.jshell.execution.Util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The COMMITS directory. */
    public static final File COMMITS = join(GITLET_DIR, "commits");
    /** The STAGE directory. */
    public static final File STAGE = join(GITLET_DIR,"stagingArea");
    /** The BLOB directory. */
    public static final File BLOB = join(GITLET_DIR,"blobs");
    /** The STAGE FOR ADDITION directory. */
    public static File addStage = join(STAGE,"addStage");
    /** The STAGE FOR REMOVAL directory. */
    public static File removeStage = join(STAGE, "removeStage");
    /** The HEAD. */
    private static File Head = new File(".gitlet/HEAD.txt");
    /** A hashmap of commits. Use Sha1 as key and hashmap of one commit as content */
    public static HashMap<String,HashMap> commitsL = new HashMap<>();
    /** The BLOB file used to contain the hashmap of committed files
     *  key is the and value is a hashmap containing
     *  Sha1 encoding of the files and version numbers of the file
     *  The inner design is also a hashmap of hashmap*/
    public static HashMap<String, String> blobFiles;
    /** persistence of blob */
    public static File blobTXT = join(BLOB,"blob.txt");
    /** containing a hashmap of filenamd and sha1 pairs in the staging area */
    public static File addTXT = join(STAGE,"toAdd.txt");
    /** containing a hashmap of filenamd and sha1 pairs in the removal area */
    public static File removeTXT = join(STAGE,"toRemove.txt");
    /** containing a hashmap of history of commits */
    public static File commitHis = join(COMMITS,"commitHis.txt");
    /** bad boy branch */
    public static File branchMaps = join(GITLET_DIR,"branch");
    public static File readcurrentBranch = join(GITLET_DIR,"currentBranch");
    public static String[] otherBranch = null;
    public static boolean mergeConflictEncountered = false;
    public static String mergedParent = "";

    public Repository() {
    }

    public void init() {
        if (GITLET_DIR.exists()) {
            System.out.print("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        else {
            GITLET_DIR.mkdir();
            COMMITS.mkdir();
            STAGE.mkdir();
            BLOB.mkdir();
            addStage.mkdir();
            removeStage.mkdir();
        }
        Commit firstCommit = new Commit();
        HashMap<String,Object> initialCommit = new HashMap<>();
        initialCommit.put("encoding",firstCommit.getEncoding());
        initialCommit.put("parent",null);
        initialCommit.put("blob",new HashMap<String, String>());
        initialCommit.put("timestamp",firstCommit.getTimestamp());
        commitsL.put(firstCommit.getEncoding(),initialCommit);
        writeObject(Head,firstCommit);
        File c = join(COMMITS, firstCommit.getEncoding());
        Utils.writeObject(c, firstCommit);

        /* creating blob */
        blobFiles = new HashMap<String,String>();
        //Utils.writeObject(blobTXT, (Serializable) blobFiles);

        /* HEAD points to the current commit */
        Utils.writeObject(Head, firstCommit);

        /* Write to Commithistory */
        HashMap toWrite = new HashMap();
        toWrite.put(firstCommit.getEncoding(),firstCommit);
        writeObject(commitHis,toWrite);

        /* creating addTXT and removeTXT */
        Utils.writeObject(addTXT,new HashMap<>());
        Utils.writeObject(removeTXT,new HashMap<>());

        /* update branch info
        *  and write to currentBranch */
        HashMap<String,Commit> toBranch = new HashMap<>();
        toBranch.put("master",firstCommit);
        Utils.writeObject(branchMaps,toBranch);
        Utils.writeContents(readcurrentBranch,"master");
    }

    public void add(String fileName) {
        File filed = new File(fileName);

        if (!filed.exists()) {
            System.out.print("File does not exist.");
            System.exit(0);
        }

        //* unstage from removal area and rewrite to removeTXT *//
        HashMap toRemove = Utils.readObject(removeTXT, HashMap.class);
        toRemove.remove(fileName);
        Utils.writeObject(removeTXT,toRemove);

        HashMap staged = Utils.readObject(addTXT, HashMap.class);
        Commit currentCommit = Utils.readObject(Head,Commit.class);
        HashMap currentBlob = currentCommit.getBlobHub();
        String workingSha = sha1(serialize(readContents(filed)));
        if (currentBlob!=null && currentBlob.containsKey(fileName)) {
            if (workingSha.equals(currentBlob.get(fileName))) {
                if (staged!=null) {
                    staged.remove(fileName);
                }
                return;  // terminate because same file exists in current commit
            }
        }
        if (staged.containsKey(fileName)) {
            staged.replace(fileName,workingSha);
        }
        else {
            staged.put(fileName, workingSha);
        }

        //* copy to ADDSTAGE *//
        try {
            Files.copy(filed.toPath(),join(addStage,fileName).toPath(),REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //* write to stageTxt *//
        Utils.writeObject(addTXT, staged);
    }

    public void commmit(String m, boolean isMergeCommit) {
        HashMap staged = Utils.readObject(addTXT, HashMap.class);
        HashMap<String,String> toRemove = Utils.readObject(removeTXT,HashMap.class);
        HashMap commitHistory = Utils.readObject(commitHis,HashMap.class);
        HashMap<String,Commit> branches = readObject(branchMaps,HashMap.class);

        if (staged.size()==0 && toRemove.size()==0) {
            System.out.print("No changes added to the commit.");
            System.exit(0);
        }
        Commit normalParent = Utils.readObject(Head,Commit.class);

        //* iterate through staged and toRemove *//
        HashMap<String, String> forBlob = new HashMap<String,String>();
        forBlob.putAll(normalParent.getBlobHub());
        forBlob.putAll(staged);
        for (String names:toRemove.keySet()) {
            forBlob.remove(names);
        }


        //* read current branch *//
        String currentbranch = readContentsAsString(readcurrentBranch);

        Commit normalCommit;
        if (isMergeCommit) {
            normalCommit = new Commit(m,normalParent, currentbranch, forBlob,branches.get(mergedParent));
        }
        else {
            normalCommit = new Commit(m,normalParent, currentbranch, forBlob);
        }
        Utils.writeObject(Head,normalCommit);

        /* create commit file in COMMITS folder */
        File commitFile = join(COMMITS,normalCommit.getEncoding());
        Utils.writeObject(commitFile, normalCommit);



        /* update branch pointer */
        branches.put(currentbranch,normalCommit);
        writeObject(branchMaps,branches);




        commitHistory.put(normalCommit.getEncoding(),normalCommit);
        writeObject(commitHis,commitHistory);
        //* doubt *//
        List<String> nameList = Utils.plainFilenamesIn(addStage);
        for (String name :nameList) {
            File theFile = join(addStage,name);
            //* stackoverflow: how to get file name w/o extension *//
            String deExtended =  name.replaceFirst("[.][^.]+$", "");
            String workingSha = sha1(serialize(readContents(theFile)));
            File traced = join(BLOB,deExtended,workingSha);
            if (traced.exists()) {
                theFile.delete();
                continue;
            }
            traced.mkdirs();
            try {
                Files.move(theFile.toPath(),join(traced,name).toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //* clearing staging area * //
        writeObject(addTXT,new HashMap<String,String>());
        writeObject(removeTXT,new HashMap<String,String>());
        for (String i:plainFilenamesIn(addStage)) {
            restrictedDelete(join(addStage,i));
        }


    }

    public void branch(String newedBranch) {
        HashMap<String, Commit> branchHist = readObject(branchMaps, HashMap.class);
        if (branchHist.containsKey(newedBranch)) {
            System.out.print("A branch with that name already exists.");
            System.exit(0);
        }
        Commit currentCommit = readObject(Head,Commit.class);
        branchHist.put(newedBranch,currentCommit);
        writeObject(branchMaps,branchHist);
    }

    public void rmBranch(String branchName) {
        String currentbranch = readContentsAsString(readcurrentBranch);
        HashMap<String,Commit> branchHist = readObject(branchMaps,HashMap.class);
        if (branchName.equals(currentbranch)) {
            System.out.print("Cannot remove the current branch.");
            System.exit(0);
        }
        if (!branchHist.containsKey(branchName)) {
            System.out.print("A branch with that name does not exist.");
            System.exit(0);
        }
        branchHist.remove(branchName);
        writeObject(branchMaps,branchHist);

    }

    public void rm(String fileName) {
        HashMap staged = Utils.readObject(addTXT, HashMap.class);
        HashMap toRemove = Utils.readObject(removeTXT,HashMap.class);
        Commit normalParent = Utils.readObject(Head,Commit.class);
        HashMap blobby = normalParent.getBlobHub();
        File fileToRemove = join(CWD,fileName);
        if (staged.containsKey(fileName)) {
            staged.remove(fileName);
            //* can't use restricted delete cuz inside .gitlet folder *//
            join(addStage,fileName).delete();
            Utils.writeObject(addTXT, staged);
            return;
        }
        if (blobby.containsKey(fileName)) {
            toRemove.put(fileName,"");
            Utils.restrictedDelete(fileToRemove);
            Utils.writeObject(removeTXT,toRemove);
            return;
        }
        System.out.print("No reason to remove the file.");
        System.exit(0);

    }

    public void log() {
        Commit currentCommit = Utils.readObject(Head, Commit.class);
        Commit pointer = currentCommit;

        String formats = "EEE MMM d HH:mm:ss yyyy Z";
        DateFormat formatOfDate = new SimpleDateFormat(formats);
        Date temp;

        while (pointer!=null) {
            if (!pointer.getMergeType()) {
                temp = pointer.getTimestamp();
                System.out.println("===");
                System.out.println("commit " + pointer.getEncoding());
                System.out.println("Date: " + formatOfDate.format(temp));
                System.out.println(pointer.getMessage());
                System.out.println("");
                pointer = pointer.getParent();
            }
            else {
                temp = pointer.getTimestamp();
                System.out.println("===");
                System.out.println("commit " + pointer.getEncoding());
                System.out.println("Merge: " +
                        pointer.getParent().getEncoding().substring(0,7)
                        +" "+ pointer.getSecondParent().getEncoding().substring(0,7));
                System.out.println("Date: " + formatOfDate.format(temp));
                System.out.println(pointer.getMessage());
                System.out.println("");
                pointer = pointer.getParent();
            }
        }
    }

    public void globalLog() {
        String formats = "EEE MMM d HH:mm:ss yyyy Z";
        DateFormat formatOfDate = new SimpleDateFormat(formats);
        Date temp;

        for (String i: plainFilenamesIn(COMMITS)) {
            if (i.equals("commitHis")||i.equals("commitHis.txt")) {
                continue;
            }
            File aCommit = join(COMMITS,i);
            Commit toPrint = readObject(aCommit,Commit.class);
            temp = toPrint.getTimestamp();
            System.out.println("===");
            System.out.println("commit "+toPrint.getEncoding());
            System.out.println("Date: "+formatOfDate.format(temp));
            System.out.println(toPrint.getMessage());
            System.out.println("");
        }
    }

    public void find(String message) {
        int counts = 0;
        for (String i: plainFilenamesIn(COMMITS)) {
            if (i.equals("commitHis")||i.equals("commitHis.txt")) {
                continue;
            }
            File aCommit = join(COMMITS,i);
            Commit toPrint = readObject(aCommit,Commit.class);
            if (message.equals(toPrint.getMessage())) {
                System.out.println(i);
                counts += 1;
            }
        }
        if (counts==0) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    public void status() {
        //* branch section *//
        System.out.println("=== Branches ===");
        String currentBranch = readContentsAsString(readcurrentBranch);
        System.out.println("*"+currentBranch);

        // iterate through other branches
        // fix later

        HashMap<String,Commit> branchHist = readObject(branchMaps,HashMap.class);
        for (String key:branchHist.keySet()) {
            if (!key.equals(currentBranch)) {
                System.out.println(key);
            }
        }

        System.out.println();
        //* staging section *//
        System.out.println("=== Staged Files ===");
        for (String i:plainFilenamesIn(addStage)) {
            System.out.println(i);
        }
        System.out.println();
        //* removal section *//
        HashMap toRemove = readObject(removeTXT,HashMap.class);
        System.out.println("=== Removed Files ===");
        for (Object i:toRemove.keySet()) {
            System.out.println(i);
        }
        System.out.println();
        //* modification section *//
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        //* untracked files section *//
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public void reset(String commitID) {
        Commit currentCommit = Utils.readObject(Head, Commit.class);
        HashMap<String,Object> commitHistory = readObject(commitHis,HashMap.class);
        if (true) {
            for (String i : commitHistory.keySet()) {
                if (i.length()>commitID.length()) {
                    if (i.substring(0,commitID.length()).equals(commitID)) {
                       commitID = i;
                }
                }
            }
        }
        if (!commitHistory.containsKey(commitID)) {
            System.out.print("No commit with that id exists.");
            System.exit(0);
        }
        Commit destCommit = (Commit) commitHistory.get(commitID);
        HashMap<String,Commit> branches = readObject(branchMaps,HashMap.class);

        untrackedChecker(currentCommit,destCommit);

        HashMap<String,String> branchedBlob = destCommit.getBlobHub();
        HashMap<String,String> currentBlob = currentCommit.getBlobHub();
        HashMap<String,String> addTrack = readObject(addTXT,HashMap.class);
        HashMap<String,String> removeTrack = readObject(removeTXT,HashMap.class);

        //* change commit in branchmaps *//
        branches.put(currentCommit.getBranch(),destCommit);
        writeObject(branchMaps,branches);

        List<String> currentFiles = plainFilenamesIn(CWD);
        for (String i:currentFiles) {
            if (currentBlob.containsKey(i)) {
                if (!branchedBlob.containsKey(i)) {
                    join(CWD,i).delete();
                }
            }
        }


        for (String fileName : branchedBlob.keySet()) {

            String deExtended =  fileName.replaceFirst("[.][^.]+$", "");
            File traced = join(BLOB, deExtended,  branchedBlob.get(fileName), fileName);
            try {
                Files.copy(traced.toPath(), join(CWD, fileName).toPath(), REPLACE_EXISTING);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

        for (String i:plainFilenamesIn(addStage)) {
            join(addStage,i).delete();
        }

        addTrack = new HashMap<String,String>();
        removeTrack = new HashMap<String,String>();

        writeObject(addTXT,addTrack);
        writeObject(removeTXT,removeTrack);

        //* change head *//
        writeObject(Head,destCommit);

    }



    //* Takes the version of the file as it exists in
    // the head commit and puts it in the working directory,
    // overwriting the version of the file that’s already
    // there if there is one.
    // The new version of the file is not staged. *//
    public void checkout(String theBranch) {
        HashMap<String, Commit> branchHist = readObject(branchMaps, HashMap.class);
        String currentBranch = readContentsAsString(readcurrentBranch);
        if (!branchHist.containsKey(theBranch)) {
            System.out.print("No such branch exists.");
            System.exit(0);
        }
        if (theBranch.equals(currentBranch)) {
            System.out.print("No need to checkout the current branch.");
            System.exit(0);
        }

        Commit currentCommit = readObject(Head, Commit.class);
        Commit destCommit = branchHist.get(theBranch);



        untrackedChecker(currentCommit,destCommit);

        HashMap<String,String> branchedBlob = destCommit.getBlobHub();
        HashMap<String,String> currentBlob = currentCommit.getBlobHub();
        HashMap<String,String> addTrack = readObject(addTXT,HashMap.class);
        HashMap<String,String> removeTrack = readObject(removeTXT,HashMap.class);



        //* Untracked Files is for files present in the working
        // directory but neither staged for addition nor tracked. *//

        for (String fileName : branchedBlob.keySet()) {

            String deExtended =  fileName.replaceFirst("[.][^.]+$", "");
            File traced = join(BLOB, deExtended,  branchedBlob.get(fileName), fileName);
            try {
                Files.copy(traced.toPath(), join(CWD, fileName).toPath(), REPLACE_EXISTING);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

        List<String> currentFiles = plainFilenamesIn(CWD);
        for (String i:currentFiles) {
            if (currentBlob.containsKey(i)) {
                if (!branchedBlob.containsKey(i)) {
                    join(CWD,i).delete();
                }
            }
        }
        addTrack = new HashMap<String,String>();
        removeTrack = new HashMap<String,String>();

        for (String i:plainFilenamesIn(addStage)) {
            join(addStage,i).delete();
        }


        writeObject(addTXT,addTrack);
        writeObject(removeTXT,removeTrack);

        //* change head *//
        writeContents(readcurrentBranch,theBranch);
        writeObject(Head,destCommit);

    }

    public void checkout(String dashes, String fileName) {
        if (!dashes.equals("--")) {
            System.out.print("Incorrect operands.");
            System.exit(0);
        }
        Commit currentCommit = Utils.readObject(Head, Commit.class);
        HashMap blobby = currentCommit.getBlobHub();
        if (!blobby.containsKey(fileName)) {
            System.out.print("File does not exist in that commit.");
            System.exit(0);
        }
        try {
            String deExtended =  fileName.replaceFirst("[.][^.]+$", "");
            File traced = join(BLOB,deExtended,(String) blobby.get(fileName),fileName);
            Files.copy(traced.toPath(),join(CWD,fileName).toPath(),REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkout(String commitID, String dashes, String fileName) {
        if (!dashes.equals("--")) {
            System.out.print("Incorrect operands.");
            System.exit(0);
        }
        Commit currentCommit = Utils.readObject(Head, Commit.class);
        HashMap<String,Object> commitHistory = readObject(commitHis,HashMap.class);
        if (true) {
            for (String i : commitHistory.keySet()) {
                if (i.length()>commitID.length()) {
                    if (i.substring(0,commitID.length()).equals(commitID)) {
                        commitID = i;
                    }
                }
            }
        }
        if (!commitHistory.containsKey(commitID)) {
            System.out.print("No commit with that id exists.");
            System.exit(0);
        }
        Commit targetCommit = (Commit) commitHistory.get(commitID);
        HashMap blobby =  targetCommit.getBlobHub();
        if (!blobby.containsKey(fileName)) {
            System.out.print("File does not exist in that commit.");
            System.exit(0);
        }
        try {
            String deExtended =  fileName.replaceFirst("[.][^.]+$", "");
            File traced = join(BLOB,deExtended,(String) blobby.get(fileName),fileName);
            Files.copy(traced.toPath(),join(CWD,fileName).toPath(),REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void MergeFileDifferentiator(HashMap<String,String> currentBlob,
                                           HashMap<String,String> branchedBlob,
                                           HashMap<String,String> splitBlob,
                                           String fileName,
                                           Commit destionationCommit) {
        //* present in all blobs *//
        if (splitBlob.containsKey(fileName)&&currentBlob.containsKey(fileName)&&branchedBlob.containsKey(fileName)) {
            // A A !A //
            String splitFile = splitBlob.get(fileName);
            String currentFile = currentBlob.get(fileName);
            String branchedFile = branchedBlob.get(fileName);

            if (splitFile.equals(currentFile) && !splitFile.equals(branchedFile)) {
                try {
                    String deExtended =  fileName.replaceFirst("[.][^.]+$", "");
                    File traced = join(BLOB,deExtended,(String) branchedBlob.get(fileName),fileName);
                    Files.copy(traced.toPath(),join(CWD,fileName).toPath(),REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                add(fileName);
                return;
            }

        }
        else if (splitBlob.containsKey(fileName) && currentBlob.containsKey(fileName) && !branchedBlob.containsKey(fileName) ){
            String splitFile = splitBlob.get(fileName);
            String currentFile = currentBlob.get(fileName);
            // A A X //
            if (splitFile.equals(currentFile)) {
                rm(fileName);
                return;
            }
        }

        else if (!splitBlob.containsKey(fileName) && !currentBlob.containsKey(fileName) && branchedBlob.containsKey(fileName) ){
            try {
                String deExtended =  fileName.replaceFirst("[.][^.]+$", "");
                File traced = join(BLOB,deExtended,(String) branchedBlob.get(fileName),fileName);
                Files.copy(traced.toPath(),join(CWD,fileName).toPath(),REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            add(fileName);
            return;
        }

        //* conflicts *//
        if (true){
            if (!splitBlob.containsKey(fileName)) {
                if (branchedBlob.containsKey(fileName)&&currentBlob.containsKey(fileName)) {
                    if (!branchedBlob.get(fileName).equals(currentBlob.get(fileName))){

                        ////////wrtie something
                        byte[] currentContents = readContents(join(CWD,fileName));
                        checkout(destionationCommit.getEncoding(),"--",fileName);
                        byte[] branchedContents = readContents(join(CWD,fileName));
                        writeContents(join(CWD,fileName),"<<<<<<< HEAD","\n",currentContents,
                                "=======","\n",branchedContents,">>>>>>>","\n");
                        mergeConflictEncountered = true;
                        add(fileName);
                    }
                }
            }
            else {
                if (branchedBlob.containsKey(fileName)&&currentBlob.containsKey(fileName)) {
                    String splitFile = splitBlob.get(fileName);
                    String currentFile = currentBlob.get(fileName);
                    String branchedFile = branchedBlob.get(fileName);
                    if (!splitFile.equals(currentFile) && !splitFile.equals(branchedFile) && !branchedFile.equals(currentFile)) {
                        ///wrtie something
                        byte[] currentContents = readContents(join(CWD,fileName));
                        checkout(destionationCommit.getEncoding(),"--",fileName);
                        byte[] branchedContents = readContents(join(CWD,fileName));
                        writeContents(join(CWD,fileName),"<<<<<<< HEAD","\n",currentContents,
                                "=======","\n",branchedContents,">>>>>>>","\n");
                        mergeConflictEncountered = true;
                        add(fileName);
                    }
                }

                else if (!currentBlob.containsKey(fileName) && branchedBlob.containsKey(fileName)) {
                    if (!branchedBlob.get(fileName).equals(splitBlob.get(fileName))) {
                        ///wrtite something
                        String currentContents = null;
                        checkout(destionationCommit.getEncoding(),"--",fileName);
                        byte[] branchedContents = readContents(join(CWD,fileName));
                        writeContents(join(CWD,fileName),"<<<<<<< HEAD","\n",currentContents,
                                "=======","\n",branchedContents,">>>>>>>","\n");
                        mergeConflictEncountered = true;
                        add(fileName);
                    }
                }
                else if (currentBlob.containsKey(fileName) && !branchedBlob.containsKey(fileName)) {
                    if (!currentBlob.get(fileName).equals(splitBlob.get(fileName))) {
                        ///write something
                        byte[] currentContents = readContents(join(CWD,fileName));
                        String branchedContents = "";
                        writeContents(join(CWD,fileName),"<<<<<<< HEAD","\n",currentContents,
                                "=======","\n",branchedContents,">>>>>>>","\n");
                        mergeConflictEncountered = true;
                        add(fileName);
                    }
                }

            }

        }

    }

    public void merge(String theBranch) {
        HashMap toAdd = readObject(addTXT,HashMap.class);
        HashMap toRemove = readObject(removeTXT,HashMap.class);
        //* uncommited changes checking *//
        if (toAdd.size()!=0 || toRemove.size()!=0) {
            System.out.print("You have uncommitted changes.");
            System.exit(0);
        }


        HashMap<String,Commit> branchHist = readObject(branchMaps,HashMap.class);
        String currentBranch = readContentsAsString(readcurrentBranch);
        Commit currentCommit = readObject(Head,Commit.class);
        Commit theBranchCommit = branchHist.get(theBranch);
        HashMap<String,Commit> commitHist = readObject(commitHis,HashMap.class);
        HashSet<String> ancestorOfCurrent = new HashSet<>();



        if (!branchHist.containsKey(theBranch)) {
            System.out.print("A branch with that name does not exist.");
            System.exit(0);
        }

        if (theBranch.equals(currentBranch)) {
            System.out.print("Cannot merge a branch with itself.");
            System.exit(0);
        }

        //* trace the split point *//
        Commit pointer =currentCommit;
        Commit splitPoint = null;

        //* taking care of two parents case *//
        HashSet<String> currentHistory = new HashSet<>();
        if (true) {
            pointer = currentCommit;
            LinkedList<Commit> fringe = new LinkedList<>();
            fringe.add(pointer);
            while (!fringe.isEmpty()) {
                Commit node = fringe.removeFirst();
                currentHistory.add(node.getEncoding());
                if (node.getParent()!=null) {
                    currentHistory.add(node.getParent().getEncoding());
                    fringe.addLast(node.getParent());
                }
                if (node.getSecondParent()!=null) {
                    currentHistory.add(node.getSecondParent().getEncoding());
                    fringe.addLast(node.getSecondParent());
                }
            }
            pointer = theBranchCommit;
            fringe.add(pointer);
            while (!fringe.isEmpty()) {
                Commit node = fringe.removeFirst();
                if (currentHistory.contains(node.getEncoding())) {
                    splitPoint = node;
                    break;
                }
                else {
                    if (node.getParent()!=null) {
                        fringe.addLast(node.getParent());
                    }
                    if (node.getSecondParent()!=null) {
                        fringe.addLast(node.getSecondParent());
                    }
                }
            }


        }

        //* check split point special cases *//
        if (splitPoint.getEncoding().equals(theBranchCommit.getEncoding())) {
            System.out.print("Given branch is an ancestor of the current branch.");
            return;
        }

        if (splitPoint.getEncoding().equals(currentCommit.getEncoding())) {
            checkout(theBranch);
            System.out.print("Current branch fast-forwarded.");
            return;
        }

        untrackedChecker(currentCommit,theBranchCommit);
        //* create file union *//
        HashSet<String> unionFiles = new HashSet<>();

        HashMap<String,String> splitBlob = splitPoint.getBlobHub();
        HashMap<String,String> branchBlob = theBranchCommit.getBlobHub();
        HashMap<String,String> currentBlob = currentCommit.getBlobHub();

        unionFiles.addAll(splitBlob.keySet());
        unionFiles.addAll(branchBlob.keySet());
        unionFiles.addAll(currentBlob.keySet());

        //* 8 cases *//

        for (String i:unionFiles) {
            MergeFileDifferentiator(currentBlob,branchBlob,splitBlob,i,theBranchCommit);
        }
        mergedParent = theBranch;
        commmit("Merged "+ theBranch+ " into " + currentBranch+".",true);
        if (mergeConflictEncountered) {
            System.out.print("Encountered a merge conflict.");
        }
    }

    /* TODO: fill in the rest of this class. */

    private static void untrackedChecker(Commit currentCommit, Commit destinationCommit) {
        List<String> currentFiles = plainFilenamesIn(CWD);
        HashMap<String,String> currentBlob = currentCommit.getBlobHub();
        HashMap<String,String> destBlob = destinationCommit.getBlobHub();
        HashMap<String,String> addMap = readObject(addTXT,HashMap.class);

        for (String fileName:currentFiles) {
            if (!currentBlob.containsKey(fileName)) {
                if (!addMap.containsKey(fileName)) {
                    if (destBlob.containsKey(fileName)) {
                        if (currentBlob.get(fileName)!=destBlob.get(fileName)) {
                            System.out.print("There is an untracked file in the way; delete it, or add and commit it first.");
                            System.exit(0);
                        }
                    }
                }
            }
        }
    }
}
