package com.bitcrunzh.generic.config.description.java;

/**
 * The version is used to indicate what can be expected to be understood by older installations and if the model has breaking changes.
 */
public class Version implements Comparable<Version>{
    private final int majorVersion;
    private final int minorVersion;

    public Version(int majorVersion, int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    @Override
    public int compareTo(Version o) {
        if(majorVersion > o.majorVersion) {
            return 1;
        }
        if(majorVersion < o.majorVersion) {
            return -1;
        }
        return Integer.compare(minorVersion, o.minorVersion);
    }

    public static Version of(int major, int minor) {
        return new Version(major, minor);
    }
}
