/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RadarBots;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
/**
 *
 * @author Henry
 */
public class InfinityLock extends AdvancedRobot {
    public void run() {
        // ...
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }
    
    public void onScannedRobot(ScannedRobotEvent e) {
        // ...
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());
    }
}
