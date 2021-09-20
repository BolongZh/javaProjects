package gitlet;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Bolong Zheng
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */

    /* Argument checker */
    private static void argumentChecker(String[] args, int requiredLen) {
        if (args.length!=requiredLen) {
            System.out.print("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // TODO: what if args is empty?
        Repository myRepo = new Repository();
        if (args==null||args.length==0) {
            System.out.print("Please enter a command.");
            System.exit(0);
        }
        if (!myRepo.GITLET_DIR.exists() && !args[0].equals("init")) {
            System.out.print("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                argumentChecker(args,1);
                myRepo.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                argumentChecker(args,2);
                myRepo.add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                if (args.length==2 && (args[1].equals("")||args[1]==null)) {
                    System.out.print("Please enter a commit message.");
                    System.exit(0);
                }
                myRepo.commmit(args[1],false);
                break;
            case "rm":
                argumentChecker(args,2);
                myRepo.rm(args[1]);
                break;

            case "log":
                argumentChecker(args,1);
                myRepo.log();
                break;

            case "checkout":
                if (args.length<2 ||args.length>4) {
                    System.out.print("Incorrect operands.");
                    System.exit(0);
                }
                if (args.length==3) {
                    myRepo.checkout(args[1],args[2]);
                }
                else if (args.length==4){
                    myRepo.checkout(args[1],args[2],args[3]);
                }
                else {
                    myRepo.checkout(args[1]);
                }
                break;

            case "global-log":
                argumentChecker(args,1);
                myRepo.globalLog();
                break;

            case "find":
                argumentChecker(args,2);
                myRepo.find(args[1]);
                break;

            case "status":
                argumentChecker(args,1);
                myRepo.status();
                break;

            case "branch":
                argumentChecker(args,2);
                myRepo.branch(args[1]);
                break;

            case "rm-branch":
                argumentChecker(args,2);
                myRepo.rmBranch(args[1]);
                break;

            case "reset":
                argumentChecker(args,2);
                myRepo.reset(args[1]);
                break;

            case "merge":
                argumentChecker(args,2);
                myRepo.merge(args[1]);
                break;
            default:
                System.out.print("No command with that name exists.");
        }
    }
}
