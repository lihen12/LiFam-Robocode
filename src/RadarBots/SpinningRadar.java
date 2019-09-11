/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RadarBots;

import robocode.*;

/**
 *
 * @author Henry
 */
public class SpinningRadar extends AdvancedRobot{
    public void run() {
        do {
            turnRadarRightRadians(Double.POSITIVE_INFINITY);
        } while (true);
    }
}
