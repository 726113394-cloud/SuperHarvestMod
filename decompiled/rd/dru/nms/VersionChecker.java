/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package rd.dru.nms;

import java.util.Arrays;
import java.util.Comparator;
import org.bukkit.Bukkit;
import rd.dru.nms.NMSHandler;
import rd.dru.nms.V1_13Handler;
import rd.dru.nms.V1_16Handler;
import rd.dru.nms.V1_17Handler;
import rd.dru.nms.V1_8Handler;
import rd.dru.nms.V1_9Handler;

public class VersionChecker {
    private static Version current;
    private static int serverVersion;

    static {
        serverVersion = 0;
    }

    public static int getServerVersion() {
        if (serverVersion != 0) {
            return serverVersion;
        }
        String a = Bukkit.getServer().getClass().getPackage().getName();
        String version = a.substring(a.lastIndexOf(46) + 1, a.lastIndexOf("_"));
        serverVersion = Integer.parseInt(version.substring(version.indexOf("_") + 1));
        return serverVersion;
    }

    public static Version getCurrentVersion() {
        if (current != null) {
            return current;
        }
        String a = Bukkit.getServer().getClass().getPackage().getName();
        if (!a.contains("_")) {
            return Version.v1_17;
        }
        String version = a.substring(a.lastIndexOf(46) + 1, a.lastIndexOf("_"));
        current = Version.getSupportVerison(Integer.parseInt(version.substring(version.indexOf("_") + 1)));
        return current;
    }

    public static enum Version {
        v1_8(8, new V1_8Handler()),
        v1_9(9, new V1_9Handler()),
        v1_13(13, new V1_13Handler()),
        v1_16(16, new V1_16Handler()),
        v1_17(17, new V1_17Handler());

        private final int id;
        private final NMSHandler nms;

        private Version(int id, NMSHandler nms) {
            this.nms = nms;
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static Version getCurrentVersionByID(int id) {
            return Arrays.stream(Version.values()).filter(i -> id == i.id).findFirst().orElse(v1_17);
        }

        public static Version getSupportVerison(int id) {
            return Arrays.stream(Version.values()).filter(i -> id >= i.getId()).min(Comparator.comparing(i -> id - i.getId())).orElse(v1_8);
        }

        public boolean isOlderThan(Version other) {
            return this.getId() > other.getId();
        }

        public NMSHandler getNMS() {
            return this.nms;
        }
    }
}

