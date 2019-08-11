/**
 * Holds information about Tokimons such as name, size, ability, strength.
 */
public class Tokimon {
    String name;
    double size;
    String ability;
    int strength;

    static int numbOfTokimon=0; //begin with 0 total tokimons
    Tokimon(){
        numbOfTokimon++; //add to existing tokimons when new tokimon created
        this.strength=0;
    }
    //print object attributes when that object is printed
    @Override
    public String toString() {
        return "Tokimon{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", ability='" + ability + '\'' +
                ", strength=" + strength +
                '}';
    }



}
