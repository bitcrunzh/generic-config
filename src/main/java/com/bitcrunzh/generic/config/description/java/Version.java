package com.bitcrunzh.generic.config.description.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The version is used to indicate what can be expected to be understood by older installations and if the model has breaking changes.
 */
public class Version implements Comparable<Version>{
    private static final Pattern VERSION_STRING_PATTERN = Pattern.compile("(?<major>\\d+)\\.(?<minor>\\d+)");
    public  static final Version DEFAULT = new Version(1, 0);
    private final int majorVersion;
    private final int minorVersion;

    public Version(int majorVersion, int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    public static Version of(String versionString) {
        Matcher matcher = VERSION_STRING_PATTERN.matcher(versionString);
        if(!matcher.matches()) {
            throw new IllegalArgumentException(String.format("versionString '%s' did not follow version string pattern'%s'", versionString, VERSION_STRING_PATTERN.pattern()));
        }
        int major = Integer.getInteger(matcher.group("major"));
        int minor = Integer.getInteger(matcher.group("minor"));
        return new Version(major, minor);
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
