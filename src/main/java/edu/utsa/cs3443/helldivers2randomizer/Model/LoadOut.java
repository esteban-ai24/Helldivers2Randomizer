package edu.utsa.cs3443.helldivers2randomizer.Model;

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

    public LoadOut(String name, String thing1, String thing2, String thing3, String thing4) {
        this(name, thing1, thing2, thing3, thing4, null, null, null, null);
    }

    public LoadOut copy() {
        return new LoadOut(name, thing1, thing2, thing3, thing4,
                image1Path, image2Path, image3Path, image4Path);
    }

    public String getName() { return name; }
    public String getThing1() { return thing1; }
    public String getThing2() { return thing2; }
    public String getThing3() { return thing3; }
    public String getThing4() { return thing4; }
    public String getImage1Path() { return image1Path; }
    public String getImage2Path() { return image2Path; }
    public String getImage3Path() { return image3Path; }
    public String getImage4Path() { return image4Path; }

    public void setName(String name) { this.name = name; }
    public void setThing1(String thing1) { this.thing1 = thing1; }
    public void setThing2(String thing2) { this.thing2 = thing2; }
    public void setThing3(String thing3) { this.thing3 = thing3; }
    public void setThing4(String thing4) { this.thing4 = thing4; }
    public void setImage1Path(String image1Path) { this.image1Path = image1Path; }
    public void setImage2Path(String image2Path) { this.image2Path = image2Path; }
    public void setImage3Path(String image3Path) { this.image3Path = image3Path; }
    public void setImage4Path(String image4Path) { this.image4Path = image4Path; }
}
