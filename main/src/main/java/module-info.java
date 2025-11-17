// excluded: requires mp3spi;
module info.rsdev.boombox.main {
    requires java.desktop;
    requires java.logging;
    requires java.prefs;
    requires info.rsdev.modules.api;
    requires info.rsdev.settings;
    requires tritonus.share;
    requires jlayer;
    requires jorbis;
    requires vorbisspi;
    requires jaudiotagger;
    
    uses info.rsdev.modules.api.Module;
}
