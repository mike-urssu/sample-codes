package ffmpeg;

import opencv.FrameExtractor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrameMerger {
    /**
     * 로컬에서 테스트 용도로 java.util.logging.Logger을 하고
     * 프로젝트에서는 org.slf4j.Logger을 사용합니다.
     */
    private static final Logger log = Logger.getLogger(FrameMerger.class.getName());

    public static void compositeFrames(Map<String, Integer> videoInfo, File sourceDirectory, File outputVideo) throws IOException, InterruptedException {
        String compositingCommand = "ffmpeg -y -r " + videoInfo.get("fps") + " -f image2 -s " + videoInfo.get("width") + "x" + videoInfo.get("height") + " -i " + new File(sourceDirectory, "%d.jpg") + " -vcodec libx264 -crf 25 -pix_fmt yuv420p -b 4M " + outputVideo;
        Process process = Runtime.getRuntime().exec(compositingCommand);
        PipeStream bufferStream = new PipeStream(process.getErrorStream(), new LoggingOutputStream(log, LoggingOutputStream.LogLevel.INFO));
        bufferStream.start();
        process.waitFor();
        bufferStream.interrupt();
        if (process.exitValue() != 0)
            throw new RuntimeException("FFMPEG: 프레임을 동영상으로 만드는 과정에서 오류가 발생했습니다.");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        File dll = new File("opencv/files/opencv/opencv_java3415.dll");
        System.load(dll.getAbsolutePath()); // 절대경로로 지정하지 않으면 UnsatisfiedLinkError 발생

        File video = new File("opencv/files/opencv/BTS.mp4");
        File sourceDirectory = new File("opencv/files/opencv/extractedFrames");
        File outputVideo = new File("opencv/files/opencv/newVideo.mp4");

        /**
         * sourceDirectory 경로에 추출된 frame이 없으면 동영상으로부터 frame을 추출합니다.
         */
        if (FileUtils.listFiles(sourceDirectory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).isEmpty())
            FrameExtractor.extractFramesFromVideo(video, sourceDirectory);

        /**
         * frame을 동영상으로 변환하는데 필요한 최소한의 정보를 추출합니다.
         */
        VideoCapture capture = new VideoCapture(video.getAbsolutePath());
        Map<String, Integer> videoInfo = new HashMap<>();
        videoInfo.put("width", (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH));
        videoInfo.put("height", (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
        videoInfo.put("fps", (int) capture.get(Videoio.CAP_PROP_FPS));

        /**
         * frame을 동영상으로 변환합니다.
         */
        compositeFrames(videoInfo, sourceDirectory, outputVideo);
    }
}

/**
 * Windows 환경에서 화면에 출력하는 버퍼가 가득 차면 Blocking 상태에 빠지게 되어 Dead Lock이 걸립니다.
 * FFMPEG이 출력하는 메시지가 에러 스트림 버퍼에 쌓이지 않도록 출력 버퍼를 실시간으로 비워주는 쓰레드를 생성해야 합니다.
 */
class PipeStream extends Thread {
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public PipeStream(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(buffer)) != -1)
                outputStream.write(buffer, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class LoggingOutputStream extends OutputStream {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(8192);
    private final Logger logger;
    private final LogLevel level;

    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR,
    }

    public LoggingOutputStream(Logger logger, LogLevel level) {
        this.logger = logger;
        this.level = level;
    }

    @Override
    public void write(int buffer) {
        if (buffer == '\n') {
            String line = outputStream.toString();
            outputStream.reset();

            switch (level) {
                /**
                 * org.slf4j.Logger를 사용하는 경우
                 * case TRACE -> logger.trace(line);
                 * case DEBUG -> logger.debug(line);
                 * case ERROR -> logger.error(line);
                 * case INFO -> logger.info(line);
                 * case WARN -> logger.warn(line);
                 */

                case TRACE:
                    logger.log(Level.FINEST, line);
                    break;
                case DEBUG:
                    logger.log(Level.FINE, line);
                    break;
                case INFO:
                    logger.log(Level.INFO, line);
                    break;
                case WARN:
                    logger.log(Level.WARNING, line);
                    break;
                case ERROR:
                    logger.log(Level.SEVERE, line);
                    break;
            }
        } else
            outputStream.write(buffer);
    }
}
