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

/**
 * Class with hardcoded values for my computer. To be made configuration options
 * in the future. This is to make things work quickly.
 */
public abstract class Hardcoded {

    /**
     * Do not instantiate this class. It is supposed to hold only constants.
     */
    private Hardcoded() {
    }

    public static final String MUSIC_FOLDER = "/home/dschoorl/Music";

    public static final String USER_EXTENSION_DIR = "/home/dschoorl/.boombox/ext";

}
