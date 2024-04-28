/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guioptimiser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Mahmoud-Uni
 */
public class GuiOptimiser {

    private static String TARGET_APP = "calculator.jar";
    //private static final String TARGET_APP = "simpleApp.jar";
    private static final String TARGET_APP_TEMP_COLOR = "temp_colour.csv";
    private static final String TARGET_APP_COLOR = "color.csv";
    private static final int TARGET_APP_RUNNINGTIME = 2000;
    private static final String JAVA_COMMAND = "java -jar ";
    private static String parentDir = "";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // first run the target app
        switch (args[0]) {
            case "calculator.jar":
                TARGET_APP = args[0].trim();
                System.out.println(args[0]);
                break;
            case "simpleApp.jar":
                TARGET_APP = args[0].trim();
                System.out.println(args[0]);
                break;
            default:
                System.out.println(args[0]);
                return;
        }
        parentDir = getParentDir();
        float result;
        switch (args[1]) {
            case "rs":
                result = randomOptimisation(Integer.parseInt(args[2]));
                break;
            case "hc":
                result = hillClimber(Integer.parseInt(args[2]));
                break;
            case "ge":
                result = genetic(Integer.parseInt(args[2]));
                break;
            default:
                result = randomOptimisation(Integer.parseInt(args[2]));
                return;
        }

        System.out.println(result);

    }
    // private 
    public static float randomOptimisation(int runs) {
        float lowestPower = 999999999; 
        ArrayList<Integer> resultArray = new ArrayList<>();
        //System.out.println(parentDir.concat(TARGET_APP));
        for (int i = 0; i < runs; i++) //RunTargetApp runTargetApp = new RunTargetApp(parentDir.concat(TARGET_APP), TARGET_APP_RUNNINGTIME);
        {
            //runApp(parentDir.concat(TARGET_APP), TARGET_APP_RUNNINGTIME);
            ColourInfo ci = changeColorAll();

            ArrayList<Integer> textColour = ci.RGB.get(19);
            ArrayList<Integer> textFieldColour = ci.RGB.get(20);
            double euclideanDistance = ColourInfo.getEuclidean(textColour, textFieldColour);

            if (euclideanDistance > 128){
                float powerResult = runApp(TARGET_APP, TARGET_APP_RUNNINGTIME);

                if (powerResult < lowestPower){
                    lowestPower = powerResult;
                    saveToCSV(parentDir.concat(TARGET_APP_TEMP_COLOR), ci.guiComponents, ci.RGB);
                }
            }
            resultArray.add((int)lowestPower);
        }
        System.out.println(resultArray);
        return lowestPower;
    }   
    public static float hillClimber(int runTimes) {
        float lowestPower = 999999999; 
        ColourInfo ci = changeColorAll(); // initial location
        ArrayList<Integer> resultArray = new ArrayList<>();
        //System.out.println(parentDir.concat(TARGET_APP));

        for (int i = 0; i < runTimes; i++) //RunTargetApp runTargetApp = new RunTargetApp(parentDir.concat(TARGET_APP), TARGET_APP_RUNNINGTIME);
        {
            ArrayList<ColourInfo> similars = ci.getSimilarSolutions();

            similars.add(ci);

            for (int k =0; k<similars.size(); k++){
                ColourInfo tempCI = similars.get(k);
                ArrayList<Integer> textColour = tempCI.RGB.get(19);
                ArrayList<Integer> textFieldColour = tempCI.RGB.get(20);
                double euclideanDistance = ColourInfo.getEuclidean(textColour, textFieldColour);
                saveToCSV(parentDir.concat(TARGET_APP_COLOR), tempCI.guiComponents, tempCI.RGB);
    
                if (euclideanDistance > 128){
                    float powerResult = runApp(TARGET_APP, TARGET_APP_RUNNINGTIME);
    
                    if (powerResult < lowestPower){
                        lowestPower = powerResult;
                        System.out.println("new lowest power");
                        System.out.println(lowestPower);
                        ci = tempCI;
                        saveToCSV(parentDir.concat(TARGET_APP_TEMP_COLOR), tempCI.guiComponents, tempCI.RGB);
                    }
                }
            }
            resultArray.add((int)lowestPower);
        }
        System.out.println(resultArray);
        return lowestPower;
    }
    public static float genetic(int runTimes) {
        float lowestPower = 999999999;
        Population pop = new Population();
        ArrayList<Integer> resultArray = new ArrayList<>();

        for (int k =0; k<5; k++){
            ColourInfo tempCI = changeColorAll();
            ArrayList<Integer> textColour = tempCI.RGB.get(19);
            ArrayList<Integer> textFieldColour = tempCI.RGB.get(20);
            double euclideanDistance = ColourInfo.getEuclidean(textColour, textFieldColour);
            saveToCSV(parentDir.concat(TARGET_APP_COLOR), tempCI.guiComponents, tempCI.RGB);

            if (euclideanDistance > 128){
                float powerResult = runApp(TARGET_APP, TARGET_APP_RUNNINGTIME);

                if (powerResult < lowestPower){
                    lowestPower = powerResult;
                    System.out.println("new lowest power");
                    System.out.println(lowestPower);
                    saveToCSV(parentDir.concat(TARGET_APP_TEMP_COLOR), tempCI.guiComponents, tempCI.RGB);
                }
                Citizen tempCit = new Citizen(tempCI, powerResult);
                pop.addOrDiscardCitizen(tempCit);
            }

        }
        for (int i = 0; i < runTimes; i++) //RunTargetApp runTargetApp = new RunTargetApp(parentDir.concat(TARGET_APP), TARGET_APP_RUNNINGTIME);
        {
            ArrayList<ColourInfo> similars = ColourInfo.getGeneticSolutions(pop.citizens);

            for (int k =0; k<similars.size(); k++){
                ColourInfo tempCI = similars.get(k);
                ArrayList<Integer> textColour = tempCI.RGB.get(19);
                ArrayList<Integer> textFieldColour = tempCI.RGB.get(20);
                double euclideanDistance = ColourInfo.getEuclidean(textColour, textFieldColour);
                saveToCSV(parentDir.concat(TARGET_APP_COLOR), tempCI.guiComponents, tempCI.RGB);
    
                if (euclideanDistance > 128){
                    float powerResult = runApp(TARGET_APP, TARGET_APP_RUNNINGTIME);
    
                    if (powerResult < lowestPower){
                        lowestPower = powerResult;
                        System.out.println("new lowest power");
                        System.out.println(lowestPower);
                        saveToCSV(parentDir.concat(TARGET_APP_TEMP_COLOR), tempCI.guiComponents, tempCI.RGB);
                    }
                    Citizen tempCit = new Citizen(tempCI, powerResult);
                    pop.addOrDiscardCitizen(tempCit);
                }
            }
            resultArray.add((int)lowestPower);
        }

        System.out.println(resultArray);


        return lowestPower;
    }

    public static float runApp(String path, int targetAppRunningtime) {
        try {
            //java -jar C:\Users\Mahmoud-Uni\Documents\NetBeansProjects\calculator\dist\calculator.jar

            //path = "\""+path+"\"";
            // System.out.println("Target App " + path);

            Runtime runTime = Runtime.getRuntime();
            Process process = runTime.exec(JAVA_COMMAND.concat(path));
            try {
                Thread.sleep(targetAppRunningtime);
                Capture capture = new Capture();
                String cLocation = capture.takeScreenShoot();
                float batterymAh = CalculateBattery.calculateChargeConsumptionPerPixel(cLocation);
                // System.out.println(batterymAh);
//                BufferedReader stdError = new BufferedReader(new
//                InputStreamReader(process.getErrorStream()));
//                String line = "";
//                while((line=stdError.readLine())!=null)
//                {
//                    System.out.println("error!");
//                    System.out.println(line);
//                }
                process.destroy();
                return batterymAh;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 999999999;
        } catch (IOException e) {
            e.printStackTrace();
            return 999999999;
        }
    }

    public static ColourInfo changeColorAll() {
        try {
            // guiComponents contains GUI components' name.
            ArrayList<String> guiComponents = new ArrayList<>();
            guiComponents.add("mainFrameColor"); // both apps
            guiComponents.add("jButton1");// both apps
            guiComponents.add("jButton2");
            guiComponents.add("jButton3");
            guiComponents.add("jButton4");
            guiComponents.add("jButton5");
            guiComponents.add("jButton6");
            guiComponents.add("jButton7");
            guiComponents.add("jButton8");
            guiComponents.add("jButton9");
            guiComponents.add("jButton10");
            guiComponents.add("jButton11");
            guiComponents.add("jButton12");
            guiComponents.add("jButton13");
            guiComponents.add("jButton14");
            guiComponents.add("jButton15");
            guiComponents.add("jButton16");
            guiComponents.add("jButton17");
            guiComponents.add("jButton18");
            
            guiComponents.add("jTextField1");// both apps
            guiComponents.add("jTextField1TextColor");// both apps

            guiComponents.add("jLabel1");// both apps

            guiComponents.add("jPanel1");// both apps
            guiComponents.add("jPanel2");
            guiComponents.add("jPanel3");
            guiComponents.add("jPanel4");
            guiComponents.add("jPanel5");

            ArrayList<ArrayList<Integer>> RGB = new ArrayList<>();
            Random randomInt = new Random();

            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));
            RGB.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{randomInt.nextInt(256), randomInt.nextInt(256), randomInt.nextInt(256)})));

            saveToCSV(parentDir.concat(TARGET_APP_COLOR), guiComponents, RGB);
            ColourInfo ci = new ColourInfo(guiComponents, RGB);
            return ci;
        } catch (Exception e) {
            e.printStackTrace();
            return new ColourInfo();
        }

    }

    public static void saveToCSV(String filePath, ArrayList<String> guiComponents, ArrayList<ArrayList<Integer>> RGB) {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(new File(filePath)));
            String line = "";
            for (int i = 0; i < guiComponents.size(); i++) {
                line += guiComponents.get(i).concat(",").concat(RGB.get(i).toString().replace("[", "").replace("]", "").replaceAll("\\s", "")) + "\n";
                //System.out.println(line);
            }
            br.write(line);
            br.flush();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getParentDir() {
        String dir = "";
        try {
            File temp = new File("temp");
            dir = temp.getAbsolutePath().replace("temp", "");
            //System.out.println(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dir;
    }
}
