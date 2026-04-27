package edu.utsa.cs3443.helldivers2randomizer.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Service class responsible for managing LoadOut objects.
 * Handles:
 * <ul>
 *     <li>Random loadout generation</li>
 *     <li>Stratagem loading and selection</li>
 *     <li>Persistence (saving/loading loadouts to files)</li>
 *     <li>Active loadout state management</li>
 * </ul>
 */
public class LoadOutService {

    /** Stores all generated loadouts for the current session */
    private static final List<LoadOut> weaponLoad = new ArrayList<>();

    /** Stores all stratagem names loaded from file */
    private static final ArrayList<String> stratagemNames = new ArrayList<>(); // Holds all 80 something names of stratagems from file

    /** Holds the currently selected 4 stratagems */
    private static final String[] chosenStratArr = new String[4]; //holds chosen stratagems for easy reference

    /** The currently active loadout */
    private static LoadOut activeLoadout;

    /**
     * Sets the active loadout.
     * A defensive copy is stored to prevent external modification.
     * @param loadout the loadout to set as active, or null to clear
     */
    public static void setActiveLoadout(LoadOut loadout) {
        activeLoadout = loadout == null ? null : loadout.copy();
    }

    /**
     * Returns a copy of the active loadout.
     * @return a copy of the active loadout, or null if none is set
     */
    public static LoadOut getActiveLoadout() {
        return activeLoadout == null ? null : activeLoadout.copy();
    }

    /**
     * Creates a new random loadout using 4 randomly selected stratagems.
     * @param name the desired name of the loadout (defaults to "Loadout" if null/blank)
     * @return the newly created LoadOut, or null if stratagems are not loaded
     */
    public static LoadOut createRandomLoadout(String name) {
        String finalName = (name == null || name.isBlank()) ? "Loadout" : name.trim();
        if (!rollChosenStratagems()){
            System.out.println("Cannot create loadout — stratagems not loaded.");
            return null; // caller must null-check
        }
        LoadOut load = new LoadOut(finalName,
                chosenStratArr[0], chosenStratArr[1],
                chosenStratArr[2], chosenStratArr[3]);
        applyChosenStratagemsToLoadout(load);
        weaponLoad.add(load);
        return load;
    }

    /**
     * Generates an image path string for a given stratagem name.
     * @param stratagemName the name of the stratagem
     * @return the corresponding image file path, or null if input is null
     */
    public static String createImgPath(String stratagemName){
        if (stratagemName == null) return null;
        return "/images/Stratagems/" + stratagemName + ".png";
    }

    /**
     * Loads stratagem names from the file "data/stratagems.txt".
     * Populates the internal stratagem list.
     */
    public static void loadStratagemNames() {
        stratagemNames.clear();

        // Reads from the data/ directory
        File file = new File("data/stratagems.txt");
        if (!file.exists()) {
            System.out.println("Stratagem file not found at: " + file.getAbsolutePath());
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine().trim();
                if (!name.isEmpty()) stratagemNames.add(name);
            }
        } catch (IOException e) {
            System.out.println("Error reading stratagem file: " + e.getMessage());
        }
    }

    /**
     * Randomly selects 4 unique stratagems from the loaded list.
     * @return true if selection was successful, false if not enough stratagems exist
     */
    public static boolean rollChosenStratagems() {
        if (stratagemNames.size() < 4) {
            System.out.println("Not enough stratagems loaded.");
            return false;
        }
        List<String> pool = new ArrayList<>(stratagemNames); // copy so we can remove without touching original
        Collections.shuffle(pool);
        for (int i = 0; i < 4; i++) {
            chosenStratArr[i] = pool.get(i);
        }
        return true;
    }

    /**
     * Applies the currently selected stratagems' image paths to a LoadOut.
     * @param load the LoadOut to update
     */
    public static void applyChosenStratagemsToLoadout(LoadOut load) {
        if (load == null) return;
        load.setImage1Path(createImgPath(chosenStratArr[0]));
        load.setImage2Path(createImgPath(chosenStratArr[1]));
        load.setImage3Path(createImgPath(chosenStratArr[2]));
        load.setImage4Path(createImgPath(chosenStratArr[3]));
    }

    /**
     * Rerolls the stratagems for an existing loadout and updates it in place.
     * @param load the LoadOut to modify
     */
    public static void rerollLoadout(LoadOut load) {
        if (load == null) return;
        rollChosenStratagems();
        load.setThing1(chosenStratArr[0]);
        load.setThing2(chosenStratArr[1]);
        load.setThing3(chosenStratArr[2]);
        load.setThing4(chosenStratArr[3]);
        applyChosenStratagemsToLoadout(load);
    }

    /**
     * Loads all saved loadouts for the currently logged-in user.
     * Data is read from a CSV file in the data directory.
     */
    public static void loadCurrentUserLoadouts() {
        weaponLoad.clear();
        User current = User.getCurrentUser();
        if (current == null) return;
        File file = new File("data/" + current.getUsername() + "_loadouts.csv");
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) weaponLoad.add(convertLineToLoadout(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a LoadOut to a specific slot for the current user.
     * @param slot the slot identifier (e.g., "slot1")
     * @param load the LoadOut to save
     */
    public static void saveSlot(String slot, LoadOut load) {
        User current = User.getCurrentUser();
        if (current == null || slot == null || load == null) return;
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, current.getUsername() + "_" + slot + ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(convertLoadoutToLine(load));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a LoadOut from a specific slot for the current user.
     * @param slot the slot identifier
     * @return the loaded LoadOut, or null if not found
     */
    public static LoadOut loadSlot(String slot) {
        User current = User.getCurrentUser();
        if (current == null || slot == null) return null;
        File file = new File("data/" + current.getUsername() + "_" + slot + ".csv");
        if (!file.exists()) return null;
        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) return convertLineToLoadout(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts a LoadOut object into a CSV string.
     * @param load the LoadOut to convert
     * @return a comma-separated string representation
     */
    private static String convertLoadoutToLine(LoadOut load) {
        return safe(load.getName()) + "," + safe(load.getThing1()) + "," + safe(load.getThing2()) + "," +
                safe(load.getThing3()) + "," + safe(load.getThing4()) + "," +
                safe(load.getImage1Path()) + "," + safe(load.getImage2Path()) + "," +
                safe(load.getImage3Path()) + "," + safe(load.getImage4Path());
    }

    /**
     * Converts a CSV string into a LoadOut object.
     * @param line the CSV line
     * @return the parsed LoadOut
     */
    private static LoadOut convertLineToLoadout(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length >= 9) {
            return new LoadOut(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8]);
        }
        return new LoadOut(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    /**
     * Safely converts a string to a non-null value.
     * @param value the input string
     * @return the original string or an empty string if null
     */
    private static String safe(String value) { return value == null ? "" : value; }
}
