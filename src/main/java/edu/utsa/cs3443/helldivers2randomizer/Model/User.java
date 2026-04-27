package edu.utsa.cs3443.helldivers2randomizer.Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Represents a user account in the Helldivers 2 Randomizer application.
 * This class handles:
 * <ul>
 *     <li>User credential storage (username and password)</li>
 *     <li>User file creation and persistence</li>
 *     <li>User authentication (sign-in)</li>
 *     <li>Tracking the currently logged-in user</li>
 * </ul>
 */
public class User {

    private String username;
    private String password;

    /** The currently logged-in user (shared application state) */
    private static User currentUser;

    /**
     * Constructs a new User object.
     * @param username the user's username
     * @param password the user's password
     */
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    /**
     * Sets the username for this user.
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the username of this user.
     * @return the username
     */
    public String getUsername() { return username; }

    /**
     * Sets the password for this user.
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the password of this user.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the currently logged-in user.
     * @return the current User, or null if no user is logged in
     */
    public static User getCurrentUser(){return currentUser;}

    /**
     * Sets the currently logged-in user.
     * Null values are ignored.
     * @param user the user to set as current
     */
    public static void setCurrentUser(User user){
        if(user != null){
            currentUser = user;
        }
    }

    /**
     * Creates a new user account file and registers the user.
     * A file is created in the "data" directory named after the username.
     * If the file already exists, registration fails.
     * @param username the desired username
     * @param password the desired password
     * @return a new User object if registration succeeds, otherwise null
     */
    public static User createUserFile(String username, String password){

        try{
            File dir = new File ("data");
            if (!dir.exists()) { dir.mkdir();}
            File file = new File(dir, username + ".csv");

            if(file.createNewFile()){
                FileWriter writer = new FileWriter(file);
                writer.write(username + ", " + password);
                writer.close();
                System.out.println("Registration Successful!");
                return new User(username, password);
            }
            else{
                System.out.println("User already exists");
                return null;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
         return null;
    }

    /**
     * Authenticates a user by reading their stored credentials from file.
     * @param username the username to check
     * @param password the password to validate
     * @return a User object if authentication succeeds, otherwise null
     */
    public static User signIn(String username, String password){

        File file = new File("data/" + username + ".csv");
        if(!file.exists()){
            System.out.println("Username not found.");
            return null;
        }
        try{
            Scanner fileReader = new Scanner(file);

            if(fileReader.hasNextLine()){
                String line = fileReader.nextLine();
                String[] userDate = line.split(", ");

                String storedUsername = userDate[0];
                String storedPassword = userDate[1];

                if(username.equals(storedUsername) && password.equals(storedPassword)){
                    System.out.println("Sign in successful!");
                    return new User(storedUsername, storedPassword);
                }
                else{
                    System.out.println("Incorrect username and/or password!");
                }
                fileReader.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
