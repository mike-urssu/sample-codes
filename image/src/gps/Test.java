package gps;

public class Test {
    private static final String local = "X:/images/images/";
    private static final String dev = "/home/Metrix/images/images/";

    public static void local() {
        String filePath = "X:/images/images/20211007152853-images/";
        System.out.println(filePath.replace(local, ""));
    }

    public static void dev() {
        String filePath = "/home/Metrix/images/images/20210927161849-labeling_competition_resizing_folder_10/005_labeling_competition1_resizing/";
        String filePath2 = "/home/Metrix/images/images/20211206223442-자율주행/Bbox_교통표지판시호등/";
        System.out.println(filePath.replace(dev, "").substring(15));
        System.out.println(filePath2.replace(dev, "").substring(15));
    }

    public static void main(String[] args) {
        dev();
    }
}
