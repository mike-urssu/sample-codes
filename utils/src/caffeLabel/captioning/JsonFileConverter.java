package caffeLabel.captioning;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import zip.Unzip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.compress.archivers.StreamingNotSupportedException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class JsonFileConverter {
    private static final Scanner scanner = new Scanner(System.in);

    private File zipDirectory;

    private File srcDirectory;

    private File destDirectory;

    private static final Unzip zip = new Unzip();

    private static final JsonHandler jsonHandler = new JsonHandler();

    private static final List<File> malformedFiles = new ArrayList<>();

    public static void main(String[] args) {
        JsonFileConverter converter = new JsonFileConverter();
        converter.setDirectories();
        converter.unzipZipFiles();
        converter.convertFiles();
        malformedFiles.forEach(System.out::println);
    }

    public void setDirectories() {
        setZipDirectory();
        setSrcDirectory();
        setDestDirectory();
    }

    private void setZipDirectory() {
        System.out.print("압축 파일 경로: ");
        String zipPath = scanner.nextLine();
        this.zipDirectory = new File(zipPath);
    }

    private void setSrcDirectory() {
        System.out.print("참조할 최상위 폴더 경로: ");
        String srcPath = scanner.nextLine();
        this.srcDirectory = new File(srcPath);
    }

    private void setDestDirectory() {
        System.out.print("json 파일을 생성할 경로: ");
        String destPath = scanner.nextLine();
        this.destDirectory = new File(destPath);
    }

    public void unzipZipFiles() {
        FileUtils.listFiles(zipDirectory, TrueFileFilter.TRUE, TrueFileFilter.TRUE).forEach(zipFile -> {
            try {
                zip.unzip(zipFile, srcDirectory);
            } catch (IOException | StreamingNotSupportedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    public void convertFiles() {
        FileUtils.listFiles(srcDirectory, TrueFileFilter.TRUE, TrueFileFilter.TRUE)
                .forEach(jsonDirectory -> FileUtils.listFiles(jsonDirectory, TrueFileFilter.TRUE, TrueFileFilter.TRUE)
                        .forEach(jsonFile -> {
                            File newJsonDirectory = new File(destDirectory, jsonFile.getParentFile().getName());
                            if (!newJsonDirectory.exists()) {
                                try {
                                    FileUtils.forceMkdir(newJsonDirectory);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            String filename = jsonFile.getName();
                            File newJsonFile = new File(newJsonDirectory, FilenameUtils.getBaseName(filename).concat("_(4_3).").concat(FilenameUtils.getExtension(filename)));
                            transformFile(jsonFile, newJsonFile);
                        }));
    }

    private void transformFile(File jsonFile, File newJsonFile) {
//        File newJsonFile = new File(destDirectory, jsonFile.getParentFile().getName() + "/" + jsonFile.getName());

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(jsonFile.toPath()), StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(newJsonFile.toPath()), StandardCharsets.UTF_8))
        ) {
            JSONObject previousJsonObject = jsonHandler.getPreviousJsonObject(reader);
            JSONObject newJsonObject = jsonHandler.getNewJsonObject(jsonFile, previousJsonObject);
            JsonElement element = JsonParser.parseString(newJsonObject.toString());
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            writer.write(gson.toJson(element));
        } catch (JSONException e) {
            e.printStackTrace();
            malformedFiles.add(newJsonFile);
            newJsonFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
