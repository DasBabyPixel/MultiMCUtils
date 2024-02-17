package de.dasbabypixel.multimcutils;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaVersionAdder {
    public static void main(String[] args) throws IOException {
        var directoryString = String.join(" ", args);
        var directory = Path.of(directoryString).toAbsolutePath();
        if (!Files.isDirectory(directory)) throw new IllegalArgumentException("Not a directory: " + directory);
        try (var stream = Files.newDirectoryStream(directory)) {
            for (var jreHome : stream) {
                if (!Files.isDirectory(jreHome)) continue;
                var jreName = jreHome.getFileName().toString();
                Advapi32Util.registryCreateKey(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Eclipse Adoptium", "JDK");
                Advapi32Util.registryCreateKey(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Eclipse Adoptium\\JDK", jreName);
                Advapi32Util.registryCreateKey(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Eclipse Adoptium\\JDK\\" + jreName, "hotspot");
                Advapi32Util.registryCreateKey(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Eclipse Adoptium\\JDK\\" + jreName + "\\hotspot", "MSI");
                Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Eclipse Adoptium\\JDK\\" + jreName + "\\hotspot\\MSI", "Path", jreHome
                        .toAbsolutePath()
                        .toString());
            }
        }
    }
}
