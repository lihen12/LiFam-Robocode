/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myfirstbot;

import java.awt.geom.Point2D;
import robocode.*;
import robocode.util.Utils;

/**
 *
 * @author Henry and Brian
 */
public class LiFam extends AdvancedRobot {
    private byte moveDirection = 1;
    private int closeToWall = 0;
    private int wallDistance = 60;
    
    public void run() {
        // Don't get too close to the walls
        addCustomEvent(new Condition("too_close_to_walls") {
            public boolean test() {
                return (
                    // close left wall
                    (getX() <= wallDistance ||
                     // close right wall
                     getX() >= getBattleFieldWidth() - wallDistance ||
                     // close bottom wall
                     getY() <= wallDistance ||
                     // close top wall
                     getY() >= getBattleFieldHeight() - wallDistance)
                );
            }
        });
        
        do {
            //...
            // Turn the radar if we have no more turn
            if (getRadarTurnRemaining() == 0.0) {
                setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
            }
            basicMovement();
            firingMechanics();
            execute();
        } while (true);  
        
    }
    
    public void onScannedRobot(ScannedRobotEvent event) {
        // ************** RADAR ************* 
        // Absolute angle towards target
        double angleToEnemy = getHeadingRadians() + event.getBearingRadians();
        
        // Subtract current radar heading to get the turn required to face the enemy, be sure it is normalized
        double radarTurn = Utils.normalRelativeAngle(angleToEnemy - getRadarHeadingRadians());
        
        // Distance we want to scan from middle of enemy to either side
        // The 36.0 is how many units from center of enemy robot it scans.
        double extraTurn = Math.min(Math.atan(36.0 / event.getDistance()), Rules.RADAR_TURN_RATE_RADIANS);
        
        // Adjust the radar turn so it goes that much further in the direction it is going to turn
        // Basically if we were going to turn it left, turn it even more left, if right, turn even more right
        // This allows us to overshoot our enemy so that we get a good sweep that will not slip
        radarTurn += (radarTurn < 0 ? -extraTurn : extraTurn);
        
        setTurnRadarRightRadians(radarTurn);
        
        // ************ FIRE ****************
        // Pulled from https://www.youtube.com/watch?v=3KWYhhdlAUs&t=16s
        // Had to figure out how to break the loop if the prediction was off of map
        double bulletPower = 3.0;
        double myX = getX();
        double myY = getY();
        double absBearing = event.getBearingRadians() + getHeadingRadians();
        double enemyX = myX + event.getDistance() * Math.sin(absBearing);
        double enemyY = myY + event.getDistance() * Math.cos(absBearing);
        double enemyHeading = event.getHeadingRadians();
        double enemyVelocity = event.getVelocity();
        
        double dTime = 0;
        double futureX = enemyX;
        double futureY = enemyY;
        
        while (((++dTime)*(20 - 3.0 * bulletPower)) < 
                Point2D.Double.distance(myX, myY, futureX, futureY)) {
            futureX += Math.sin(enemyHeading) * enemyVelocity;
            futureY += Math.cos(enemyHeading) * enemyVelocity;
            
            // how to exit loop if futureX or futureY is too small/big
            // 18 is half of the robot's dimensions
            // make sure we don't predict outside of battlefield
            if(futureX < 18.0 
                    || futureX < 18.0
                    || futureX > getBattleFieldWidth() - 18.0
                    || futureY > getBattleFieldHeight() - 18.0) {
                futureX = Math.min(Math.max(18.0, futureX), getBattleFieldWidth() - 18.0);  
                futureY = Math.min(Math.max(18.0, futureY), getBattleFieldHeight() - 18.0);
                break;
            }
        }
        double theta = Utils.normalAbsoluteAngle(Math.atan2(futureX - myX, 
                futureY - myY));
        
        setTurnGunRightRadians(Utils.normalRelativeAngle(theta 
                - getGunHeadingRadians()));
       
        setFire(bulletPower);
        
        //setTurnGunRight(efficientBearing(getHeading() - getGunHeading() + event.getBearing()));
        
        if (event.getDistance() < 400) {
            setTurnRight(efficientBearing(event.getBearing() + 90 /*- (15 * moveDirection)*/));
        } else {
            setTurnRight(efficientBearing(event.getBearing()));
        }
    }
    
    public void onCustomEvent(CustomEvent e) {
        if (e.getCondition().getName().equals("too_close_to_walls")) {
            if (closeToWall <= 0) {
                // if we weren't already dealing with the walls, we are now
                closeToWall += wallDistance;
                
                setMaxVelocity(0); // stop!!!
            }
        }
    }
    
    public void onHitWall(HitWallEvent e) { 
        System.out.println("HIT WALL"); 
    }

    public void onHitRobot(HitRobotEvent e) { 
        closeToWall = 0; 
    }
    
    public void onHitByBullet(HitByBulletEvent e) {
        double currentVel = getVelocity();
        
        setInterruptible(true);
        if (e.getVelocity() < getVelocity()) {
            setMaxVelocity(e.getVelocity() + 2);
            if (getVelocity() > Rules.MAX_VELOCITY) {
                setMaxVelocity(Rules.MAX_VELOCITY);
            }
        } else {
            setMaxVelocity(e.getVelocity() - 2);
            if (getVelocity() < Rules.MAX_VELOCITY) {
                setMaxVelocity(currentVel);
            }
        }
        turnLeft(90 - e.getBearing());
    }

    public void basicMovement() {

        // if we're close to the wall, eventually, we'll move away
        if (closeToWall > 0) {
            closeToWall--;
        }

        // switch directions if velocity is 0
        // move away from original direction
        if (getVelocity() == 0) {
                setMaxVelocity(8);
                moveDirection *= -1;
                setAhead(10000 * moveDirection);
        }
    }
    
    public void firingMechanics() {
    
    }
    
    // Allows us to locate bearing in shortest possible angle
    public double efficientBearing(double angle) {
        while (angle > 180) {
            angle -= 360;
        }
            
        while (angle < -180) {
            angle += 360;
        }
        return angle;
    }
}
