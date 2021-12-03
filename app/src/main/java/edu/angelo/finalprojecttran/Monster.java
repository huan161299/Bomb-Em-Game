package edu.angelo.finalprojecttran;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

/**
 * Script for the monster object inside the game.
 */
public class Monster {
    /**
     * Position x of the monster on the game's map.
     */
    private int x;

    /**
     * Position y of the monster on the game's map.
     */
    private int y;

    /**
     * Health of the monster. I choose it default is 2.
     */
    private int health = 2;

    /**
     * Indicate if the monster is attacking or not (since the monster has a cooldown of attacking). If it is already attacking, then do not shooting / creating out more spike until the cooldown is over.
     */
    private boolean isAttacking;

    /**
     * Default constructor to create a monster on the game's map.
     * @param x position x on the game's map.
     * @param y position y on the game's map.
     */
    public Monster(int x, int y) {
        this.x = x;
        this.y = y;
        isAttacking = false;
    }

    /**
     * @return position x on the game's map.
     */
    public int valueX() {
        return x;
    }

    /**
     * @return position y on the game's map.
     */
    public int valueY() {
        return y;
    }

    /**
     * @return the health amount of the current monster.
     */
    public int heathAmount() {
        return health;
    }

    /**
     * @param damage deal damage and reduce the current monster's health.
     */
    public void receiveDamage(int damage) {
        health -= damage;
    }

    /**
     * Call the monster to attack (spawning 4 Spikes in 4 direction) only if the monster is not attacking. If the monster already attacks, it will have to wait
     *      for a cooldown before it can attack again.
     * @param spikes Insert all of the spikes into the SpikeSet so that it can be keep track and draw out in the World object.
     */
    public void attack(SpikeSet spikes) {
        //If the monster is not attacking, attack now.
        if (!isAttacking) {
            isAttacking = true;
            //Spawn 4 spike.
            spikes.addSpike(x * 32 + 16, y * 32 + 16, World.UP);
            spikes.addSpike(x * 32 + 16, y * 32 + 16, World.LEFT);
            spikes.addSpike(x * 32 + 16, y * 32 + 16, World.RIGHT);
            spikes.addSpike(x * 32 + 16, y * 32 + 16, World.DOWN);

            /**
             * Create a new CountDownTimer to make the monster stop attacking in a period of time before it can attack again (cooldown). We create a new
             *      Handler (timer) and Looper here so that we can use the CountDownTimer code here (since it can only be used in the main activity).
             *      References for using the Handler and Looper: "https://stackoverflow.com/questions/32283811/cant-create-handler-inside-thread-that-has-not-called-looper-prepare-in-count"
             */
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {

                    /**
                     * a CountDownTimer that deny the execution of a code until reaching a certain time limit, here I make it to wait for 3 seconds before isAttacking = false (meaning that the monster can attack again).
                     */
                    new CountDownTimer((int)(World.monsterAttackCoolDown * 1000.0), 1000) {
                        @Override
                        /**
                         * Execute the code in between the countDownInterval, since I do need to do anything in between, this code is blank.
                         */
                        public void onTick(long millisUntilFinished) {
                            //do nothing.
                        }

                        /**
                         * Execute the code after the waiting interval ends. Here I make the monster to be able to attack again after waiting for the cooldown (3 seconds).
                         */
                        @Override
                        public void onFinish() {
                            isAttacking = false;
                        }
                    }.start();
                }
            });
        }
    }
}
