package guioptimiser;

import java.util.ArrayList;

public class Population {
    ArrayList<Citizen> citizens;
    float highestpower;
    public Population(){
        citizens = new ArrayList<>();
        highestpower = 0;
    };

    public void addOrDiscardCitizen(Citizen c){
        if(citizens.size() < 5){
            citizens.add(c);
            highestpower = c.power > highestpower ? c.power : highestpower;
            System.out.println("Added due to small pop " + highestpower);
        }else if(c.power < highestpower){
            System.out.println(c.power);
            float newHighest = 0;
            int forRemoval = 0;

            for(int i =0; i<5; i++){
                if(citizens.get(i).power == highestpower){
                    forRemoval = i;
                }else{
                    newHighest = citizens.get(i).power > newHighest ? citizens.get(i).power : newHighest; 
                }
            }
            citizens.remove(forRemoval);
            citizens.add(c);
            if (newHighest < c.power){
                highestpower = c.power;
            }else{
                highestpower = newHighest;
            }
            System.out.println("Added due to good score " + highestpower);
        }else{
            // do nothing
        }
        System.out.println("data");
        for (int i = 0; i<citizens.size(); i++){
            System.out.println(citizens.get(i).power);
        }
    }
}
