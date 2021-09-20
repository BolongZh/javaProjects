package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;


/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable{
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    private String encoding;

    private Commit parent;

    private Date timestamp;
    /** Files of this commit */
    private HashMap<String,String> blobHub;

    private String branch;

    private boolean isMergeCommit = false;

    private Commit secondParent = null;

    /* TODO: fill in the rest of this class. */

    /* Special constructor at init*/
    public Commit() {
        message = "initial commit";
        parent = null;
        blobHub = new HashMap<String, String>();
        branch = "master";
        timestamp = new Date(0);
        encoding = Utils.sha1(Utils.serialize(this));
    }

    /* Constructor for normal commits*/
    public Commit(String message,Commit parent, String branch,
                  HashMap<String,String> blobHub) {
        this.message = message;
        this.parent = parent;
        this.branch = branch;
        this.blobHub = blobHub;
        this.timestamp = new Date();
        this.encoding = Utils.sha1(Utils.serialize(this));
    }

    public Commit(String message, Commit parent, String branch,
                  HashMap<String,String> blobHub, Commit secondParent) {
        this.message = message;
        this.parent = parent;
        this.branch = branch;
        this.blobHub = blobHub;
        this.timestamp = new Date();
        this.isMergeCommit = true;
        this.secondParent = secondParent;
        this.encoding = Utils.sha1(Utils.serialize(this));
    }
    /* Below are get methods for instance variables*/
    public String getEncoding() {
        return this.encoding;
    }

    public String getMessage() {
        return this.message;
    }

    public Commit getParent() {
        return this.parent;
    }

    public String getBranch() {
        return this.branch;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public HashMap getBlobHub() {
        return this.blobHub;
    }

    public boolean getMergeType() {return this.isMergeCommit;}

    public Commit getSecondParent() {return this.secondParent;}
}
