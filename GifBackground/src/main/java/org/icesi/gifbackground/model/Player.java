package org.icesi.gifbackground.model;

public class Player {
    private String position;
    private int energy;

    public Player(String position, int energy) {
        this.position = position;
        this.energy = energy;
    }

    public String getPosition() {
        return position;
    }

    public void move(String newPosition) {
        this.position = newPosition;
    }

    public int getEnergy() {
        return energy;
    }

    public void decreaseEnergy(int amount) {

        if (energy >= amount) {
            this.energy -= amount;
        } else {
            this.energy = 0;
        }
    }

}
