package edu.utsa.cs3443.helldivers2randomizer.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadOutService {

    private static final List<LoadOut> weaponLoad = new ArrayList<>();
    private static LoadOut activeLoadout;

    public static void setActiveLoadout(LoadOut loadout) {
        activeLoadout = loadout == null ? null : loadout.copy();
    }
    public static LoadOut getActiveLoadout() {
        return activeLoadout == null ? null : activeLoadout.copy();
    }

    private static final String[] IMAGE_PATHS = {
            "/images/Orbital_380MM_HE_Barrage.png",
            "/images/Orbital_EMS_Strike.png",
            "/images/Eagle_Strafing_Run.png",
            "/images/Railgun.png"
    };

    private static final String[] wep1 = {"Raygun", "Keyblade", "SG-225 Breaker", "hawktuaher gun"};
    private static final String[] wep2 = {"Mambogun", "HernandeeznutsGun", "VettersGun", "NaeNaeGun", "ViggleAiGun", "UTSAFOREVERgun"};
    private static final String[] things1 = {"thing1", "thing2", "thing3", "thing4"};
    private static final String[] things2 = {"things1", "things2", "things3", "things4"};

    public static LoadOut createRandomLoadout(String name) {
        String finalName = (name == null || name.isBlank()) ? "Loadout" : name.trim();
        LoadOut load = new LoadOut(finalName, randomFrom(wep1), randomFrom(wep2), randomFrom(things1), randomFrom(things2));
        assignRandomImages(load);
        weaponLoad.add(load);
        return load;
    }

    public static void assignRandomImages(LoadOut load) {
        if (load == null) return;
        load.setImage1Path(randomFrom(IMAGE_PATHS));
        load.setImage2Path(randomFrom(IMAGE_PATHS));
        load.setImage3Path(randomFrom(IMAGE_PATHS));
        load.setImage4Path(randomFrom(IMAGE_PATHS));
    }

    public static void rerollLoadout(LoadOut load) {
        if (load == null) return;
        load.setGun1(randomFrom(wep1));
        load.setGun2(randomFrom(wep2));
        load.setThing3(randomFrom(things1));
        load.setThing4(randomFrom(things2));
        assignRandomImages(load);
    }

    public static void saveCurrentUserLoadouts() {
        User current = User.getCurrentUser();
        if (current == null) return;
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, current.getUsername() + "_loadouts.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (LoadOut load : weaponLoad) {
                writer.write(convertLoadoutToLine(load));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return safe(load.getName()) + "," + safe(load.getGun1()) + "," + safe(load.getGun2()) + "," +
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
