package caffeLabel.captioning;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class JsonHandler {
    private static final Map<String, Category> categories = new Excel().getCategories(new File("C:\\Users\\Administrator\\Desktop\\caffe-label\\captioning\\4-3_json_categories_20220819.xlsx"));

    public JSONObject getPreviousJsonObject(BufferedReader reader) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) builder.append(line);
        return new JSONObject(builder.toString());
    }

    public JSONObject getNewJsonObject(File jsonFile, JSONObject previousObject) throws JSONException {
        JSONObject dataSet = new JSONObject();

        {     // info
            JSONObject previousInfo = previousObject.getJSONObject("info");
            dataSet.put("info", previousInfo);
        }

        {     // category
            JSONArray previousCategories = previousObject.getJSONArray("categories");

            for (int i = 0; i < previousCategories.length(); i++) {
                JSONObject previousCategory = previousCategories.getJSONObject(i);

                String filename = jsonFile.getName();
                String object = filename.substring(filename.indexOf('('), filename.indexOf(')'));
                Category category = categories.get(object);

                previousCategory.put("supercategory", category.getSuperCategory());
                previousCategory.put("id", category.getId());
                previousCategory.put("name", category.getName());
            }

            dataSet.put("categories", previousCategories);
        }

        {     // images
            JSONArray previousImages = previousObject.getJSONArray("images");

            JSONArray images = new JSONArray();
            for (int i = 0; i < previousImages.length(); i++) {
                JSONObject previousImage = previousImages.getJSONObject(i);

                JSONObject image = new JSONObject();
                image.put("id", previousImage.get("id"));
                image.put("height", previousImage.get("height"));
                image.put("width", previousImage.get("width"));

                image.put("file_name", FilenameUtils.getName(previousImage.getString("file_name")));
                images.put(image);
            }

            dataSet.put("images", images);
        }

        // annotations
        {
            JSONArray previousAnnotations = previousObject.getJSONArray("annotations");

            for (int i = 0; i < previousAnnotations.length(); i++) {
                JSONObject previousAnnotation = previousAnnotations.getJSONObject(i);
                JSONArray previousTexts = previousAnnotation.getJSONArray("text");
                for (int j = 0; j < previousTexts.length(); j++) {
                    JSONObject previousText = previousTexts.getJSONObject(j);
                    previousText.remove("text_id");
                }
            }

            dataSet.put("annotations", previousAnnotations);
        }

        return dataSet;
    }
}
