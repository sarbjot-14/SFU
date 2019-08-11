package ca.cmpt213.as2;

/**
 * This class is used to create instances of Tokimon from json files. Each feedback
 * of a Tokimon is converted into a java object.
 */
public class Tokimon {
    String name;
    String id;

    TheCompatibility compatibility;

    public class TheCompatibility{
        double score;
        String comment;

    }
}
