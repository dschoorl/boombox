/*
 * Copyright 2017 Red Star Development.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.rsdev.boombox;

import info.rsdev.modules.Module;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dave Schoorl
 */
public class ModuleManager {
    
    private static final ModuleManager INSTANCE = new ModuleManager();
    
    private Logger logger = Logger.getLogger(ModuleManager.class.getName());
    
    private ServiceLoader<Module> loader;
    
    private ModuleManager() {
        this.loader = ServiceLoader.load(Module.class);
        this.loader.forEach(module -> logger.log(Level.INFO, "loaded: {0} - {1}", new Object[]{module.getName(), module}));
    }
    
    public static ModuleManager getInstance() {
        return INSTANCE;
    }
    
}
