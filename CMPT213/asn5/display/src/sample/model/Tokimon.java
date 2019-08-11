package sample.model;

public class Tokimon {
    public int id;
    public String name;
    public String ability;//Fly Fire Water Electric Freeze
    public String color;
    public int strength;
    public double height;
    public double weight;
    public int health;

    public Tokimon(int id,String name,String ability,double height,double weight,int strength, String color, int health  ){
        this.id = id;
        this.name = name;
        this.ability = ability;
        this.height = height;
        this.weight = weight;
        this.strength = strength;
        this.color = color;
        this.health = health;
    }

}
