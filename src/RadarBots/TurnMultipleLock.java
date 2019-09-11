/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RadarBots;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 *
 * @author Henry
 */
public class TurnMultipleLock extends AdvancedRobot{
    public void run() {
        // ..
        
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
        do {
            // Check for new targets.
            // Only necessary for Narrow Lock because sometimes one radar is already
            // pointed at enemy and our onScannedRobot code doesn't end up telling
            // it to turn, so the system doesn't automatically call scan() for us
            scan();
        } while (true);
    }
    
    public void onScannedRobot(ScannedRobotEvent e) {
        double radarTurn = 
                // Absolute bearing to target
                getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();
        setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
        
        // ...
    }
}
