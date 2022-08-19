package caffeLabel.captioning;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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

import org.apache.commons.compress.archivers.StreamingNotSupportedException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class JsonFileConverter {
    private static final File zipDirectory = new File("C:\\Users\\Administrator\\Desktop\\captioning\\jsonFiles\\old");

    private static final File srcDirectory = new File("C:\\Users\\Administrator\\Desktop\\captioning\\jsonFiles\\old");

    private static final File destDirectory = new File("C:\\Users\\Administrator\\Desktop\\captioning\\jsonFiles\\new");

    private static final Zip zip = new Zip();

    private static final JsonHandler jsonHandler = new JsonHandler();

    private static final List<File> malformedFiles = new ArrayList<>();

    public static void main(String[] args) {
        JsonFileConverter converter = new JsonFileConverter();

        converter.unzipZipFiles();

        FileUtils.listFiles(srcDirectory, TrueFileFilter.TRUE, TrueFileFilter.TRUE)
                .forEach(jsonDirectory ->
                        FileUtils.listFiles(jsonDirectory, TrueFileFilter.TRUE, TrueFileFilter.TRUE)
                                .forEach(jsonFile -> {
                                            File newJsonDirectory = new File(destDirectory, jsonFile.getParentFile().getName());
                                            if (!newJsonDirectory.exists()) {
                                                try {
                                                    FileUtils.forceMkdir(newJsonDirectory);
                                                } catch (IOException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                            converter.transformFile(destDirectory, jsonFile);
                                        }
                                ));

        malformedFiles.forEach(System.out::println);
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

    public void transformFile(File destDirectory, File jsonFile) {
        File newJsonFile = new File(destDirectory, jsonFile.getParentFile().getName() + "/" + jsonFile.getName());

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(jsonFile.toPath()), StandardCharsets.UTF_8));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(newJsonFile.toPath()), StandardCharsets.UTF_8))
        ) {
            JSONObject previousJsonObject = jsonHandler.getPreviousJsonObject(reader);
            JSONObject newJsonObject = jsonHandler.getNewJsonObject(previousJsonObject);
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
