package mainapp;

import java.util.regex.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class MyApp {

    // Method to check if the .git directory exists
    public static String directoryExists() {
        String gitDirectoryExists;
        try {
            System.out.println("Checking if .git directory exists...");
            gitDirectoryExists = executeCommand("git rev-parse --is-inside-work-tree").trim();
            System.out.println("Git Directory Exists: " + gitDirectoryExists);
            if (gitDirectoryExists.equals("true")) {
                return "true";
            } else {
                return "false";
            }
        } catch (Exception e) {
            System.out.println("Error in directoryExists method: " + e.getMessage());
            return "";
        }
    }

    public static String checkCommitMessages() {
        String commitLog;
        try {
            System.out.println("Checking for specific commit messages...");
            // Get the commit log
            commitLog = executeCommand("git log --oneline").trim();
            System.out.println("Commit log: " + commitLog);

            // Compile patterns for each specific commit message
            Pattern firstCommitPattern = Pattern.compile("First commit", Pattern.CASE_INSENSITIVE);
            Pattern secondCommitPattern = Pattern.compile("Second commit", Pattern.CASE_INSENSITIVE);
            Pattern thirdCommitPattern = Pattern.compile("Third commit", Pattern.CASE_INSENSITIVE);

            // Match against the commit log
            Matcher firstCommitMatcher = firstCommitPattern.matcher(commitLog);
            Matcher secondCommitMatcher = secondCommitPattern.matcher(commitLog);
            Matcher thirdCommitMatcher = thirdCommitPattern.matcher(commitLog);

            boolean firstFound = firstCommitMatcher.find();
            boolean secondFound = secondCommitMatcher.find();
            boolean thirdFound = thirdCommitMatcher.find();

            // Return results based on matches
            if (firstFound && secondFound && thirdFound) {
                System.out.println("All required commit messages found.");
                return "true";
            } else {
                System.out.println("One or more required commit messages are missing:");
                if (!firstFound) System.out.println("- Missing: First commit");
                if (!secondFound) System.out.println("- Missing: Second commit");
                if (!thirdFound) System.out.println("- Missing: Third commit");
                return "false";
            }
        } catch (Exception e) {
            System.out.println("Error in checkCommitMessages method: " + e.getMessage());
            return "";
        }
    }

    // Method to check if a branch called feature_branch exists
    public static String featureBranchExists() {
        String featureBranchExists;
        try {
            System.out.println("Checking if feature_branch exists...");
            featureBranchExists = executeCommand("git branch --list feature_branch");
            System.out.println("Feature Branch Exists: " + featureBranchExists);
            if (featureBranchExists.contains("feature_branch")) {
                return "true";
            } else {
                return "false";
            }
        } catch (Exception e) {
            System.out.println("Error in featureBranchExists method: " + e.getMessage());
            return "";
        }
    }

    // Method to check if a commit has been successfully reverted
    public static String revertSuccess() {
        String revertSuccess;
        try {
            System.out.println("Checking if the last commit has 'Revert' in the message...");
            revertSuccess = executeCommand("git log --oneline | head -n 1").trim();
            System.out.println("Latest commit: " + revertSuccess);
            // Checking if the latest commit message contains "Revert"
            if (revertSuccess.contains("Revert")) {
                return "true";
            } else {
                return "false";
            }
        } catch (Exception e) {
            System.out.println("Error in revertSuccess method: " + e.getMessage());
            return "";
        }
    }

    public static void main(String[] args) {
        try {
            // Check if .git directory exists
            String gitDirectoryExists = directoryExists();
            if (gitDirectoryExists.equals("true")) {
                System.out.println("Git repository initialized successfully.");
            } else {
                System.out.println("Git repository not initialized.");
                return;
            }

            // Check for at least one commit
            String checkCommit = checkCommitMessages();
            if (checkCommit.equals("true")) {
                System.out.println("Changes have been committed.");
            } else {
                System.out.println("No changes committed.");
                return;
            }

            // Check if feature_branch exists
            String featureBranchExists = featureBranchExists();
            if (featureBranchExists.equals("true")) {
                System.out.println("feature_branch exists.");
            } else {
                System.out.println("feature_branch does not exist.");
            }

            // Check if revert was successful
            String revertSuccess = revertSuccess();
            if (revertSuccess.equals("true")) {
                System.out.println("revert operation was successful.");
            } else {
                System.out.println("revert operation failed.");
            }

        } catch (Exception e) {
            System.out.println("Error in main method: " + e.getMessage());
        }
    }

    private static String executeCommand(String command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", command);
        System.out.println("Executing command: " + command);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitVal = process.waitFor();
        System.out.println("Command executed with exit code: " + exitVal);
        if (exitVal == 0) {
            return output.toString();
        } else {
            System.out.println("Command failed with exit code: " + exitVal);
            throw new RuntimeException("Failed to execute command: " + command);
        }
    }
}
