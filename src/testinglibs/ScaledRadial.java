/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testinglibs;

import eu.hansolo.steelseries.gauges.Radial;

/**
 *
 * @author aribdhuka
 */
public class ScaledRadial extends Radial {
    
    double scale;
    
    public ScaledRadial(double scale) {
        this.scale = scale;
    }
    
    public void setScaledValue(double value) {
        this.setValue(value/scale);
        this.setLcdValue(value);
    }
    
}
