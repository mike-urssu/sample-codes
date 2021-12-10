package opencv;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.io.File;

public class FrameExtractor {
    public static void extractFramesFromVideo(File video, File destination) {
        VideoCapture videoCapture = new VideoCapture(video.getAbsolutePath());
        if (videoCapture.isOpened()) {
            int imageIndex = 0;
            Mat frame = new Mat();
            while (videoCapture.read(frame))
                Imgcodecs.imwrite(new File(destination, imageIndex++ + ".jpg").getAbsolutePath(), frame);
            videoCapture.release();
        } else
            throw new RuntimeException("동영상(" + video + ")으로부터 frame을 추출할 수 없습니다.");
    }

    public static void main(String[] args) {
        File dll = new File("opencv/files/opencv/opencv_java3415.dll");
        System.load(dll.getAbsolutePath()); // 절대경로로 지정하지 않으면 UnsatisfiedLinkError 발생

        File video = new File("opencv/files/opencv/BTS.mp4");
        File destination = new File("opencv/files/opencv/extractedFrames");

//        for (File file : FileUtils.listFiles(destination, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE))
//            file.delete();

        extractFramesFromVideo(video, destination);
    }
}
