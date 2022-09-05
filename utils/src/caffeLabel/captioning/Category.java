package caffeLabel.captioning;

public class Category {
    private final String filename;
    private final String folderName;
    private final String superCategory;
    private final String id;
    private final String name;

    public Category(String filename, String folderName, String superCategory, String id, String name) {
        this.filename = filename;
        this.folderName = folderName;
        this.superCategory = superCategory;
        this.id = id;
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getSuperCategory() {
        return superCategory;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
