package edu.utsa.cs3443.helldivers2randomizer.Model;

/**
 * Represents a LoadOut consisting of a name and four selectable items,
 * along with image paths associated with each item.
 *
 * This class is used to store and manage loadout configurations
 * for the Helldivers 2 randomizer application.
 */
public class LoadOut {
    private String name;
    private String thing1;
    private String thing2;
    private String thing3;
    private String thing4;
    private String image1Path;
    private String image2Path;
    private String image3Path;
    private String image4Path;

    /**
     * Constructs a LoadOut with a name, four items, and image paths.
     *
     * @param name the name of the loadout
     * @param thing1 the first item
     * @param thing2 the second item
     * @param thing3 the third item
     * @param thing4 the fourth item
     * @param image1Path file path for the first item's image
     * @param image2Path file path for the second item's image
     * @param image3Path file path for the third item's image
     * @param image4Path file path for the fourth item's image
     */
    public LoadOut(String name, String thing1, String thing2, String thing3, String thing4,
                   String image1Path, String image2Path, String image3Path, String image4Path) {
        this.name = name;
        this.thing1 = thing1;
        this.thing2 = thing2;
        this.thing3 = thing3;
        this.thing4 = thing4;
        this.image1Path = image1Path;
        this.image2Path = image2Path;
        this.image3Path = image3Path;
        this.image4Path = image4Path;
    }

    /**
     * Constructs a LoadOut with a name and four items (no image paths).
     *
     * @param name the name of the loadout
     * @param thing1 the first item
     * @param thing2 the second item
     * @param thing3 the third item
     * @param thing4 the fourth item
     */
    public LoadOut(String name, String thing1, String thing2, String thing3, String thing4) {
        this(name, thing1, thing2, thing3, thing4, null, null, null, null);
    }

    /**
     * Creates and returns a deep copy of this LoadOut.
     *
     * @return a new LoadOut object with identical field values
     */
    public LoadOut copy() {
        return new LoadOut(name, thing1, thing2, thing3, thing4,
                image1Path, image2Path, image3Path, image4Path);
    }

    /** @return the name of the loadout */
    public String getName() { return name; }

    /** @return the first item */
    public String getThing1() { return thing1; }

    /** @return the second item */
    public String getThing2() { return thing2; }

    /** @return the third item */
    public String getThing3() { return thing3; }

    /** @return the fourth item */
    public String getThing4() { return thing4; }

    /** @return the image path for the first item */
    public String getImage1Path() { return image1Path; }

    /** @return the image path for the second item */
    public String getImage2Path() { return image2Path; }

    /** @return the image path for the third item */
    public String getImage3Path() { return image3Path; }

    /** @return the image path for the fourth item */
    public String getImage4Path() { return image4Path; }

    /**
     * Sets the name of the loadout.
     * @param name the new name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Sets the first item.
     * @param thing1 the new first item
     */
    public void setThing1(String thing1) { this.thing1 = thing1; }

    /**
     * Sets the second item.
     * @param thing2 the new second item
     */
    public void setThing2(String thing2) { this.thing2 = thing2; }

    /**
     * Sets the third item.
     * @param thing3 the new third item
     */
    public void setThing3(String thing3) { this.thing3 = thing3; }

    /**
     * Sets the fourth item.
     * @param thing4 the new fourth item
     */
    public void setThing4(String thing4) { this.thing4 = thing4; }

    /**
     * Sets the image path for the first item.
     * @param image1Path the new image path
     */
    public void setImage1Path(String image1Path) { this.image1Path = image1Path; }

    /**
     * Sets the image path for the second item.
     * @param image2Path the new image path
     */
    public void setImage2Path(String image2Path) { this.image2Path = image2Path; }

    /**
     * Sets the image path for the third item.
     * @param image3Path the new image path
     */
    public void setImage3Path(String image3Path) { this.image3Path = image3Path; }

    /**
     * Sets the image path for the fourth item.
     * @param image4Path the new image path
     */
    public void setImage4Path(String image4Path) { this.image4Path = image4Path; }

}
