package opencv;

import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class Mosaic {
    public static void blurImageAsRectangle(File image, JSONArray jsonArray) {
        Mat frame = Imgcodecs.imread(image.getAbsolutePath());
        int height = frame.height();
        int width = frame.width();

        if (!jsonArray.isEmpty()) {
            for (Object jsonObject : jsonArray) {
                Position position = new Position(width, height, (JSONObject) jsonObject);
                Mosaic.blurArea(frame, position);
            }
        }

//        Imgcodecs.imwrite(image.getAbsolutePath(), frame);
        Imgcodecs.imwrite("opencv/files/opencv/after.jpg", frame);    // 이미지 생성
    }

    public static void blurImageAsOval(File image, JSONArray jsonArray) {
        Mat frame = Imgcodecs.imread(image.getAbsolutePath());
        int height = frame.height();
        int width = frame.width();

        Mat frameToBlur = frame.clone();
        blur(frameToBlur, (Math.max(width, height) / 100 + 1) * 15);

        Mat maskToBlur = Mat.zeros(new Size(width, height), CvType.CV_8U);
        Mat maskNotToBlur = Mat.zeros(new Size(width, height), CvType.CV_8U);

        if (!jsonArray.isEmpty()) {
            for (Object jsonObject : jsonArray) {
                Position position = new Position(width, height, (JSONObject) jsonObject);
                Imgproc.ellipse(maskToBlur, new Point(position.centerX, position.centerY), new Size(position.longAxis, position.shortAxis), position.tilt, 0, 360, new Scalar(255.0), -1);
            }
        }
        Core.bitwise_not(maskToBlur, maskNotToBlur);
        Core.bitwise_and(frame, frame, frame, maskNotToBlur);
        Core.bitwise_and(frameToBlur, frameToBlur, frame, maskToBlur);
        Imgcodecs.imwrite("opencv/files/opencv/after.jpg", frame);    // 이미지 생성
    }

    private static void blurArea(Mat frame, Position position) {
        Rect rect = new Rect(position.x, position.y, position.w, position.h);
        Mat cropFrame = new Mat(frame, rect);
        blur(cropFrame, (Math.max(position.w, position.h) / 100 + 1) * 15);
    }

    private static void blur(Mat mat, int kernel) {
        Imgproc.blur(mat, mat, new Size(kernel, kernel), new Point(-1, -1));
    }

    public static void main(String[] args) {
        File dll = new File("./opencv/files/opencv/opencv_java3415.dll");
        System.load(dll.getAbsolutePath()); // 절대경로로 지정하지 않으면 UnsatisfiedLinkError 발생

        {
            File image = new File("./opencv/files/opencv/before.jpg");
            String positions = "[{\"h\": 0.06960241529676649, \"w\": 0.02927313910590276, \"x\": 0.16291005876329212, \"y\": 0.2494452158610026}, " +
                    "{\"h\": 0.0690373526679145, \"w\": 0.02672186957465278, \"x\": 0.7295771280924479, \"y\": 0.2780910491943359}," +
                    " {\"h\": 0.06547402275933163, \"w\": 0.026346588134765623, \"x\": 0.5763871086968316, \"y\": 0.27711024814181856}," +
                    " {\"h\": 0.061332151624891494, \"w\": 0.02858793470594618, \"x\": 0.8017860412597656, \"y\": 0.24235543145073785}, " +
                    "{\"h\": 0.06379873487684462, \"w\": 0.02645327250162757, \"x\": 0.3346620771620009, \"y\": 0.2610181596544054}," +
                    " {\"h\": 0.06588456895616315, \"w\": 0.027840020921495182, \"x\": 0.2578818851047092, \"y\": 0.2546725379096137}," +
                    " {\"h\": 0.06739073859320746, \"w\": 0.02813699510362413, \"x\": 0.4174386766221788, \"y\": 0.254796748691135}, " +
                    "{\"h\": 0.06603597005208334, \"w\": 0.025339847140842017, \"x\": 0.5061041090223525, \"y\": 0.2833660337660048}," +
                    " {\"h\": 0.06261988745795356, \"w\": 0.02607879638671875, \"x\": 0.6419187333848742, \"y\": 0.282080438401964}]";
            JSONArray jsonArray = new JSONArray(positions);
            blurImageAsRectangle(image, jsonArray);
        }

        {
            File image = new File("./opencv/files/opencv/italic.jpg");
            String positions = "[{\"h\": 0.3696501288124324, \"w\": 0.20344053851907265, \"x\": 0.5033657442034405, \"y\": 0.3044959694174354}, {\"h\": 0.25928696085764147, \"w\": 0.1286462228870606, \"x\": 0.1503365744203441, \"y\": 0.3244411202526386}]";
            JSONArray jsonArray = new JSONArray(positions);
            blurImageAsOval(image, jsonArray);
        }

        /**
         * 모자이크 확인용 코드
         * Mat blurredImage = Imgcodecs.imread("./opencv/files/opencv/after.jpg");
         * HighGui.imshow("after", blurredImage);
         * HighGui.waitKey(0);
         */
    }
}

class Position {
    public int x;
    public int y;
    public int w;
    public int h;

    public int centerX;
    public int centerY;

    public int longAxis;
    public int shortAxis;
    public int tilt;

    public Position(int width, int height, JSONObject jsonObject) {
        this.x = (int) (width * Double.parseDouble(jsonObject.get("x").toString()));
        this.y = (int) (height * Double.parseDouble(jsonObject.get("y").toString()));
        this.w = (int) (width * Double.parseDouble(jsonObject.get("w").toString()));
        this.h = (int) (height * Double.parseDouble(jsonObject.get("h").toString()));

        this.centerX = x + w / 2;
        this.centerY = y + h / 2;

        longAxis = Math.max(w, h);
        shortAxis = Math.min(w, h);
        tilt = w > h ? 0 : 90;
    }

    public String toString() {
        return "(x, y, w, h): (" + x + ", " + y + ", " + w + ", " + h + ")";
    }
}
