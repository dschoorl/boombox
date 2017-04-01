/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.rsdev.settings.api;

import info.rsdev.modules.Module;

/**
 *
 * @author dschoorl
 */
public class SettingsServices implements Module {
    
    public SettingsServices() {
        System.out.println("Creating SettingsServices");
        new RuntimeException().printStackTrace(System.out);
    }
    
    @Override
    public String getName() {
        return "info.rsdev.settings.SettingsServices";
    }

    @Override
    public String getVersionString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
