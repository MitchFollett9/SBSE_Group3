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
            System.out.println("Added due to small pop");
        }else if(c.power < highestpower){
            System.out.println("Added due to good score");
            System.out.println(c.power);
            float newHighest = 0;
            for(int i =0; i<5; i++){
                newHighest = citizens.get(i).power > newHighest ? citizens.get(i).power : newHighest; 
                if(citizens.get(i).power == highestpower){
                    citizens.remove(i);
                    citizens.add(c);
                }
            }
            if (newHighest < c.power){
                highestpower = c.power;
            }else{
                highestpower = newHighest;
            }
        }else{
            // do nothing
        }
    }
}
