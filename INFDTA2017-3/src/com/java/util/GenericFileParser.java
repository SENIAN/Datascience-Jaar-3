package util;


import Algorithm.Model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed on 4/15/2017.
 */
public class GenericFileParser {
    public List<Model> readDataFile(File file) {
        List<Model> xyAxisList = new ArrayList<>();
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String temp;
            String[] tempXY = null;
            Model model;
            while ((temp = br.readLine()) != null) {
                tempXY = temp.split(",");
                model = new Model();
                model.setX(Integer.parseInt(tempXY[0]));
                model.setY(Integer.parseInt(tempXY[1]));
                xyAxisList.add(model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xyAxisList;
    }
}
