package edu.utsa.cs3443.helldivers2randomizer.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class LoadOutService {

    private static final List<LoadOut> weaponLoad = new ArrayList<>();
    private static final ArrayList<String> stratagemNames = new ArrayList<>(); // Holds all 80 something names of stratagems from file
    private static final String[] chosenStratArr = new String[4]; //holds chosen stratagems for easy reference
    private static LoadOut activeLoadout;

    public static void setActiveLoadout(LoadOut loadout) {
        activeLoadout = loadout == null ? null : loadout.copy();
    }
    public static LoadOut getActiveLoadout() {
        return activeLoadout == null ? null : activeLoadout.copy();
    }

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

    public static String createImgPath(String stratagemName){
        if (stratagemName == null) return null;
        return "/images/Stratagems/" + stratagemName + ".png";
    }
    //reads stratagems.txt and stores names in ArrayList
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
            System.out.println("Loaded " + stratagemNames.size() + " stratagems.");
        } catch (IOException e) {
            System.out.println("Error reading stratagem file: " + e.getMessage());
        }
    }

    // picks 4 unique stratagems and stores them in chosenStratArr
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

    // builds the 4 image paths from chosenStratArr and applies them to a LoadOut
    public static void applyChosenStratagemsToLoadout(LoadOut load) {
        if (load == null) return;
        load.setImage1Path(createImgPath(chosenStratArr[0]));
        load.setImage2Path(createImgPath(chosenStratArr[1]));
        load.setImage3Path(createImgPath(chosenStratArr[2]));
        load.setImage4Path(createImgPath(chosenStratArr[3]));
    }

    //rerolls existing loadout in place
    public static void rerollLoadout(LoadOut load) {
        if (load == null) return;
        rollChosenStratagems();
        load.setThing1(chosenStratArr[0]);
        load.setThing2(chosenStratArr[1]);
        load.setThing3(chosenStratArr[2]);
        load.setThing4(chosenStratArr[3]);
        applyChosenStratagemsToLoadout(load);
    }

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

    private static String convertLoadoutToLine(LoadOut load) {
        return safe(load.getName()) + "," + safe(load.getThing1()) + "," + safe(load.getThing2()) + "," +
                safe(load.getThing3()) + "," + safe(load.getThing4()) + "," +
                safe(load.getImage1Path()) + "," + safe(load.getImage2Path()) + "," +
                safe(load.getImage3Path()) + "," + safe(load.getImage4Path());
    }

    private static LoadOut convertLineToLoadout(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length >= 9) {
            return new LoadOut(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8]);
        }
        return new LoadOut(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }

    private static String safe(String value) { return value == null ? "" : value; }
    private static String randomFrom(String[] items) { return items[(int) (Math.random() * items.length)]; }
}
