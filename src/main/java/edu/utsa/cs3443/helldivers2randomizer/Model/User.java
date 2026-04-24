package edu.utsa.cs3443.helldivers2randomizer.Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class User {

    private String username;
    private String password;
    private static User currentUser;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public static User getCurrentUser(){return currentUser;}

    public static void setCurrentUser(User user){
        if(user != null){
            currentUser = user;
        }
    }

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
