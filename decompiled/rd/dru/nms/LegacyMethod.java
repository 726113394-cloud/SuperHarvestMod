/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package rd.dru.nms;

import java.lang.reflect.Constructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LegacyMethod {
    private static int in;
    private static int stay;
    private static int out;

    public static void sendTitle(Player player, String title, String subtitle, int in, int stay, int out) {
        LegacyMethod.in = in;
        LegacyMethod.stay = stay;
        LegacyMethod.out = out;
        LegacyMethod.titleSub(player, title, true);
        LegacyMethod.titleSub(player, subtitle, false);
    }

    public static void sendActionBar(Player player, String message) {
        try {
            Constructor<?> constructor = LegacyMethod.getNMSClass("PacketPlayOutChat").getConstructor(LegacyMethod.getNMSClass("IChatBaseComponent"), Byte.TYPE);
            Object icbc = LegacyMethod.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + message + "\"}");
            Object packet = constructor.newInstance(icbc, (byte)2);
            Object entityPlayer = player.getClass().getMethod("getHandle", new Class[0]).invoke((Object)player, new Object[0]);
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            playerConnection.getClass().getMethod("sendPacket", LegacyMethod.getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke((Object)player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", LegacyMethod.getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void titleSub(Player player, String message, boolean isTitle) {
        try {
            Object e = LegacyMethod.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
            Object chatMessage = LegacyMethod.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + message + "\"}");
            Constructor<?> subConstructor = LegacyMethod.getNMSClass("PacketPlayOutTitle").getConstructor(LegacyMethod.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], LegacyMethod.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            Object titlePacket = subConstructor.newInstance(e, chatMessage, in, stay, out);
            LegacyMethod.sendPacket(player, titlePacket);
            e = LegacyMethod.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField(String.valueOf(isTitle ? "" : "SUB") + "TITLE").get(null);
            chatMessage = LegacyMethod.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + message + "\"}");
            subConstructor = isTitle ? LegacyMethod.getNMSClass("PacketPlayOutTitle").getConstructor(LegacyMethod.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], LegacyMethod.getNMSClass("IChatBaseComponent")) : LegacyMethod.getNMSClass("PacketPlayOutTitle").getConstructor(LegacyMethod.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], LegacyMethod.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
            titlePacket = isTitle ? subConstructor.newInstance(e, chatMessage) : subConstructor.newInstance(e, chatMessage, Math.round((float)in / 20.0f), Math.round((float)stay / 20.0f), Math.round((float)out / 20.0f));
            LegacyMethod.sendPacket(player, titlePacket);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

