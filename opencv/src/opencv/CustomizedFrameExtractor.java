package opencv;

import encryption.SHA256;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.*;

public class CustomizedFrameExtractor {
    public static final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

    public static void extractFramesFromVideo(File video, File destination, int second) throws IOException {
        File encodedVideo = new File(video.getParent(), SHA256.encrypt(FilenameUtils.removeExtension(video.getName())) + "." + FilenameUtils.getExtension(video.getName()));
        video.renameTo(encodedVideo);

        VideoCapture videoCapture = new VideoCapture(encodedVideo.getAbsolutePath());
        {
            int fps = (int) videoCapture.get(Videoio.CAP_PROP_FPS);
            int width = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH);
            int height = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT);

            System.out.println("fps: " + fps);
            System.out.println("width: " + width);
            System.out.println("height: " + height);
        }

        int fps = (int) videoCapture.get(Videoio.CAP_PROP_FPS);
        if (videoCapture.isOpened()) {
            int imageIndex = 0;
            Mat frame = new Mat();
            while (videoCapture.read(frame)) {
                if (imageIndex % (fps * second) == 0) {
                    writer.write("second: " + imageIndex / fps);
                    writer.newLine();
                    writer.write("filePath: " + new File(destination, imageIndex + ".jpg"));
                    writer.newLine();
                    Imgcodecs.imwrite(new File(destination, imageIndex + ".jpg").getAbsolutePath(), frame);
                }
                imageIndex++;
            }
            videoCapture.release();
            writer.flush();
        } else
            throw new CannotOpenVideoException(video);
        encodedVideo.renameTo(video);
    }

    public static void main(String[] args) throws IOException {
        File dll = new File("opencv/files/opencv/opencv_java3415.dll");
        System.load(dll.getAbsolutePath()); // 절대경로로 지정하지 않으면 UnsatisfiedLinkError 발생

        File video = new File("opencv/files/opencv/BTS.mp4");
        File destination = new File("opencv/files/opencv/extractedFrames");

        FileUtils.deleteDirectory(destination);
        destination.mkdirs();

        int unitSecond = 3;
        extractFramesFromVideo(video, destination, unitSecond);
    }
}
