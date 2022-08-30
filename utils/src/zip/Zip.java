package zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class Zip {
    public void zip(File zipFile, File directory) throws IOException {
        if (!isValid(directory))
            return;

        try (ZipOutputStream outputStream = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()), StandardCharsets.UTF_8)) {
            for (File file : FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE)) {
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    String filename = file.getAbsolutePath().replace(directory.getAbsolutePath() + File.separator, "");
                    outputStream.putNextEntry(new ZipEntry(filename));

                    int length;
                    byte[] buffer = new byte[1024];
                    while ((length = inputStream.read(buffer)) > 0)
                        outputStream.write(buffer, 0, length);
                    outputStream.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValid(File directory) throws IOException {
        if (!directory.exists())
            throw new FileNotFoundException(directory.getAbsolutePath());
        if (!directory.isDirectory())
            throw new NotDirectoryException(directory.getAbsolutePath());
        return true;
    }

    public static void main(String[] args) throws IOException {
        File zipFile = new File("utils/files/zip/zip.zip");
        File directory = new File("utils/files/zip/zip할 파일");

        Zip zip = new Zip();
        zip.zip(zipFile, directory);
    }
}
