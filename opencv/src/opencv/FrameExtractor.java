package opencv;

import encryption.SHA256;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.io.IOException;

public class FrameExtractor {
    public static void extractFramesFromVideo(File video, File destination) {
        File encodedVideo = new File(video.getParent(), SHA256.encrypt(FilenameUtils.removeExtension(video.getName())) + "." + FilenameUtils.getExtension(video.getName()));
        video.renameTo(encodedVideo);

        VideoCapture videoCapture = new VideoCapture(encodedVideo.getAbsolutePath());
        if (videoCapture.isOpened()) {
            int imageIndex = 0;
            Mat frame = new Mat();
            while (videoCapture.read(frame))
                Imgcodecs.imwrite(new File(destination, imageIndex++ + ".jpg").getAbsolutePath(), frame);
            videoCapture.release();
        } else {
            encodedVideo.renameTo(video);
            throw new CannotOpenVideoException(video);
        }
        encodedVideo.renameTo(video);
    }

    public static void main(String[] args) throws IOException {
        File dll = new File("opencv/files/opencv/opencv_java3415.dll");
        System.load(dll.getAbsolutePath()); // 절대경로로 지정하지 않으면 UnsatisfiedLinkError 발생

        File video = new File("opencv/files/opencv/BTS.mp4");
        File destination = new File("opencv/files/opencv/extractedFrames");

        FileUtils.deleteDirectory(destination);
        destination.mkdirs();

        extractFramesFromVideo(video, destination);
    }
}

class CannotOpenVideoException extends RuntimeException {
    public CannotOpenVideoException(File video) {
        super("동영상(" + video + ")으로부터 frame을 추출할 수 없습니다.\n경로에 한글이 포함되는지 확인하세요.");
    }
}
