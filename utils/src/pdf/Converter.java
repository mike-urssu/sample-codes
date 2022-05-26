package pdf;

import java.awt.datatransfer.MimeTypeParseException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

public class Converter {
    private static final int DPI = 600;

    public void convertPdfIntoImage(File file, File directory) throws IOException, MimeTypeParseException {
        if (!file.exists())
            throw new FileNotFoundException(file.getAbsolutePath());

        if (!FilenameUtils.getExtension(file.getName()).equals("pdf"))
            throw new MimeTypeParseException("pdf 파일이 아닙니다.");

        FileUtils.deleteDirectory(directory);
        Files.createDirectories(directory.toPath());

        try (
            PDDocument pdf = PDDocument.load(file)
        ) {
            PDFRenderer renderer = new PDFRenderer(pdf);
            int pageSize = pdf.getNumberOfPages();
            for (int page = 0; page < pageSize; page++) {
                BufferedImage image = renderer.renderImageWithDPI(page, DPI, ImageType.RGB);
                String filename = directory + File.separator + page + ".png";
                ImageIOUtil.writeImage(image, filename, DPI);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, MimeTypeParseException {
        File pdf = new File("utils/files/pdf/INV05.pdf");
        File directory = new File("utils/files/pdf/INV05");

        Converter converter = new Converter();
        converter.convertPdfIntoImage(pdf, directory);
    }
}
