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

import java.util.prefs.Preferences;

/**
 * A set of constants that the core BoomBox uses as keys into the {@link Preferences}
 */
public abstract class PrefKeys {
    
    /**
     * Do not instantiate. This class only holds contants.
     */
    private PrefKeys() {}
    
    public static final String DATADIR_KEY = "DataDir";
    
}
