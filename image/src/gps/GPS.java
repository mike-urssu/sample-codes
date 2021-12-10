package gps;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class GPS {
    public static GeoLocation getLocationFromImage(File image) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(image);
            if (metadata.containsDirectoryOfType(GpsDirectory.class)) {
                GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
                return gpsDirectory.getGeoLocation();
            } else
                return null;
        } catch (IOException | ImageProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void copyImageWithLocation(File source, File destination, GeoLocation location) throws IOException {
        if (location == null) {
            FileUtils.copyFile(source, destination);
            return;
        }

        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destination))) {
            TiffOutputSet outputSet = new TiffOutputSet();
            outputSet.setGPSInDegrees(location.getLongitude(), location.getLatitude());

            new ExifRewriter().updateExifMetadataLossless(source, outputStream, outputSet);
        } catch (ImageWriteException | IOException | ImageReadException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        File image = new File("./image/files/desktop.jpg");
        File copiedImage = new File("./image/files/copy.jpg");
        if (copiedImage.exists())
            copiedImage.delete();

        GeoLocation location = getLocationFromImage(image);
        if (location != null) {
            System.out.println("latitude: " + location.getLatitude());
            System.out.println("longitude: " + location.getLongitude());
        }
        copyImageWithLocation(image, copiedImage, location);
    }
}
