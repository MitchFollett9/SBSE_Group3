package guioptimiser;
/* 
 * citizens are part of a population, but they denote a colour scheme and its power
 */
public class Citizen {
    ColourInfo colourInfo;
    float power;
    public Citizen(ColourInfo ci, float p){
        colourInfo = ci;
        power = p;
    }
}
