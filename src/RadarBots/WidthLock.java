/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RadarBots;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 *
 * @author Henry
 */
public class WidthLock extends AdvancedRobot {
    public void run() {
        // ...
        do {
            //...
            // Turn the radar if we have no more turn
            if (getRadarTurnRemaining() == 0.0) {
                setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
            }
            execute();
        } while (true);
        
        // ...
    }
    
    public void onScannedRobot(ScannedRobotEvent e) {
        // ...
        
        // Absolute angle towards target
        double angleToEnemy = getHeadingRadians() + e.getBearingRadians();
        
        // Subtract current radar heading to get the turn required to face the enemy, be sure it is normalized
        double radarTurn = Utils.normalRelativeAngle(angleToEnemy - getRadarHeadingRadians());
        
        // Distance we want to scan from middle of enemy to either side
        // The 36.0 is how many units from center of enemy robot it scans.
        double extraTurn = Math.min(Math.atan(36.0 / e.getDistance()), Rules.RADAR_TURN_RATE_RADIANS);
        
        // Adjust the radar turn so it goes that much further in the direction it is going to turn
        // Basically if we were going to turn it left, turn it even more left, if right, turn even more right
        // This allows us to overshoot our enemy so that we get a good sweep that will not slip
        radarTurn += (radarTurn < 0 ? -extraTurn : extraTurn);
        
        // Turn the radar
        setTurnRadarRightRadians(radarTurn);
        
        // ...
    }
}
