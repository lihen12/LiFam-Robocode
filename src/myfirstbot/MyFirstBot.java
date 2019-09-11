/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myfirstbot;
import robocode.*;

/**
 *
 * @author Henry
 */
public class MyFirstBot extends Robot {
    public void run() {
        while(true) {
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }
    
    public void onScannedRobot(ScannedRobotEvent e) {
        if (e.getDistance() < 200) {
            fire(3.0);
        } else {
            fire(1.0);
        }
    }
    
    public void onHitByBullet(HitByBulletEvent e) {
        turnRight(45);
        back(10);
    }
    
    public void onHitWall(HitWallEvent e) {
        //back(20);
    }
    
    public void onHitRobot(HitRobotEvent e) {
        back(50);
        ahead(100);
    }
}