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
    private static final Map<String, String> categoryMap = new Excel().getCategoryMap(new File("C:\\Users\\Administrator\\Desktop\\captioning\\4-3_json_categories_20220819.xlsx"));

    public JSONObject getPreviousJsonObject(BufferedReader reader) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) builder.append(line);
        return new JSONObject(builder.toString());
    }

    public JSONObject getNewJsonObject(JSONObject previousObject) throws JSONException {
        JSONObject dataSet = new JSONObject();

        {     // info
            JSONObject previousInfo = previousObject.getJSONObject("info");

//            JSONObject info = new JSONObject();
//            info.put("description", previousInfo.get("description"));
//            info.put("version", previousInfo.get("version"));
//            info.put("date_year", previousInfo.get("date_year"));

            dataSet.put("info", previousInfo);
        }

        {     // category
            JSONArray previousCategories = previousObject.getJSONArray("categories");

//            JSONArray categories = new JSONArray();
//            for (int i = 0; i < previousCategories.length(); i++) {
//                JSONObject category = new JSONObject();
//
//                JSONObject previousCategory = new JSONObject(previousCategories.get(i).toString());
//                category.put("supercategory", previousCategory.get("supercategory"));
//                category.put("id", previousCategory.get("id"));
//                category.put("name", previousCategory.get("name"));
//
//                categories.put(category);
//            }

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

                String filename = FilenameUtils.getName(previousImage.getString("file_name"));
                String object = filename.substring(12, filename.length() - 4);
                image.put("file_name", "/원천데이터/image" + "/" + categoryMap.get(object) + "/" + filename);

                images.put(image);
            }

            dataSet.put("images", images);
        }

        // annotations
        {
            JSONArray previousAnnotations = previousObject.getJSONArray("annotations");

//            JSONArray annotations = new JSONArray();
//
//            JSONObject annotation = new JSONObject();
//            annotation.put("id", previousAnnotations.get("id"));
//            annotation.put("image_id", previousAnnotations.get("image_id"));
//            annotation.put("category_id", previousAnnotations.get("category_id"));
//            annotation.put("iscrowd", previousAnnotations.get("iscrowd"));
//            annotation.put("bbox", previousAnnotations.get("bbox"));
//
//            JSONArray texts = new JSONArray();
//
//            JSONArray previousTexts = previousAnnotations.getJSONArray("text");
//            for (int i = 0; i < previousTexts.length(); i++) {
//                JSONObject text = new JSONObject();
//
//                JSONObject previousText = new JSONObject(previousTexts.get(i).toString());
//                text.put("text_id", previousText.get("text_id"));
//                text.put("english", previousText.get("english").toString().trim());
//                text.put("korean", previousText.get("korean").toString().trim());
//
//                texts.put(text);
//            }
//
//            annotation.put("text", texts);
//            annotations.put(annotation);

            dataSet.put("annotations", previousAnnotations);
        }

        return dataSet;
    }
}
