package ca.cmpt213.as2;
import java.util.*;

/**
 * This class is used to take the format of the json document and turn
 * it into a java object.
 * It uses the Tokimon class to create objects of Tokimon and inserts it into
 * team.
 */
public class TokimonTeam {
    List<Tokimon> team = new ArrayList<Tokimon>();
    String extra_comments;

    public int whichTeam;
    public int visited;

    TokimonTeam(){
        whichTeam=0;
        visited =0;

    }


}
