package caffeLabel.captioning;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.StreamingNotSupportedException;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Zip {
    private static final ArchiveStreamFactory factory = new ArchiveStreamFactory();

    public void unzip(File zipFile, File directory) throws IOException, StreamingNotSupportedException {
        if (!zipFile.exists() || !zipFile.isFile())
            throw new FileNotFoundException(zipFile + "이 존재하지 않습니다.");

        String extension = FilenameUtils.getExtension(zipFile.getName());
        switch (extension) {
            case "zip":
            case "tar":
                unzipZipOrTar(zipFile, directory);
                break;
            case "7z":
                unzip7z(zipFile, directory);
                break;
            default:
                throw new StreamingNotSupportedException(extension);
        }
    }

    private void unzipZipOrTar(File zipFile, File directory) {
        String zipFileName = FilenameUtils.getBaseName(zipFile.getName()).substring(9);

        try (ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(getArchiveName(zipFile), Files.newInputStream(zipFile.toPath()))) {
            ArchiveEntry archiveEntry;
            while ((archiveEntry = archiveInputStream.getNextEntry()) != null) {
                if (archiveEntry.getName().startsWith("라벨링"))
                    downstreamFile(new File(directory, zipFileName), archiveEntry, archiveInputStream);
            }
        } catch (IOException | ArchiveException e) {
            e.printStackTrace();
        }
    }

    private String getArchiveName(File zipFile) throws StreamingNotSupportedException {
        String archiveName;
        String extension = FilenameUtils.getExtension(zipFile.getName());
        switch (extension) {
            case "zip":
                archiveName = ArchiveStreamFactory.ZIP;
                break;
            case "tar":
                archiveName = ArchiveStreamFactory.TAR;
                break;
            default:
                throw new StreamingNotSupportedException(extension);
        }
        return archiveName;
    }

    public void unzip7z(File zipFile, File directory) {
        try (SevenZFile sevenZFile = new SevenZFile(zipFile)) {
            SevenZArchiveEntry archiveEntry;
            while ((archiveEntry = sevenZFile.getNextEntry()) != null)
                downstreamFile(directory, archiveEntry, sevenZFile.getInputStream(archiveEntry));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downstreamFile(File directory, ArchiveEntry archiveEntry, InputStream inputStream) throws IOException {
        File file = new File(directory, archiveEntry.getName().replace("라벨링", ""));

        File parentFile = file.getParentFile();
        if (!parentFile.exists()) FileUtils.forceMkdir(parentFile);

        if (archiveEntry.isDirectory()) FileUtils.forceMkdir(file);
        else downstreamFile(file, inputStream);
    }

    private void downstreamFile(File file, InputStream inputStream) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(file.toPath()))) {
            byte[] buffer = new byte[1024];
            int size;
            while ((size = inputStream.read(buffer)) > 0) outputStream.write(buffer, 0, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
