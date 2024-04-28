package guioptimiser;

import java.util.ArrayList;

public class ColourInfo {
    ArrayList<String> guiComponents;
    ArrayList<ArrayList<Integer>> RGB;    
    ColourInfo(ArrayList<String> gc, ArrayList<ArrayList<Integer>> r){
        guiComponents = gc;
        RGB = r;
    }
    ColourInfo(){
        RGB = new ArrayList<>();
        guiComponents = new ArrayList<>();
    }
    private ColourInfo getSingleSimilar(int colour, int movement){        
        ArrayList<ArrayList<Integer>> RGBtemp = new ArrayList<>();
        for (int i = 0; i < RGB.size(); i ++){
            ArrayList<Integer> rgbtempvalues = new ArrayList<>();
            for (int j = 0; j < 3; j ++){
                int temp = colour == j ? RGB.get(i).get(j) + movement : RGB.get(i).get(j);
                int trimmedValue = temp > 180 ? 180 : temp < 0 ? 0 : temp;
                rgbtempvalues.add(trimmedValue);
            }            
            RGBtemp.add(rgbtempvalues);
        }
        return new ColourInfo(guiComponents, RGBtemp);
    }
    public ArrayList<ColourInfo> getSimilarSolutions(){
        ArrayList<ColourInfo> returnValue = new ArrayList<>();

        returnValue.add(getSingleSimilar(0, -50));
        returnValue.add(getSingleSimilar(0, 50));
        returnValue.add(getSingleSimilar(1, -50));
        returnValue.add(getSingleSimilar(1, 50));
        returnValue.add(getSingleSimilar(2, -50));
        returnValue.add(getSingleSimilar(2, 50));

        return returnValue;
    }
    private static ColourInfo getOffSpring(ColourInfo parent1, ColourInfo parent2){
        ArrayList<ArrayList<Integer>> RGBtemp = new ArrayList<>();
        for (int i = 0; i < parent1.RGB.size(); i ++){
            ArrayList<Integer> rgbtempvalues = new ArrayList<>();
            for (int j = 0; j < 3; j ++){
                int temp = (parent1.RGB.get(i).get(j) + parent2.RGB.get(i).get(j))/2;
                rgbtempvalues.add(temp);
            }            
            RGBtemp.add(rgbtempvalues);
        }
        return new ColourInfo(parent1.guiComponents, RGBtemp);
    }
    public static ArrayList<ColourInfo> getGeneticSolutions(ArrayList<Citizen> oringialPopulation){
        ArrayList<ColourInfo> returnValue = new ArrayList<>();

        for (int i = 0; i < oringialPopulation.size(); i ++){
            for (int j = i; j < oringialPopulation.size(); j ++){
                if(i != j){
                    returnValue.add(getOffSpring(oringialPopulation.get(i).colourInfo, oringialPopulation.get(j).colourInfo));
                }
            }
        }

        return returnValue;
    }
    public static double getEuclidean(ArrayList<Integer> a, ArrayList<Integer> b) {
        
        int distanceSum = 0;
        for (int j = 0; j < 3; j++){
            int distance = a.get(j) - b.get(j);
            distanceSum = distanceSum + (distance * distance);
        }
        return Math.sqrt((double)distanceSum);
    }
}
