# Xdolf Forge 1.21.10 — incomplete development port

This is **source code, not a finished or verified Minecraft mod**. It targets
Minecraft **1.21.10**, Forge **60.1.0**, and **Java 21** using Forge's official MDK.
The original Minecraft 1.12.2 client in `../minecraft` remains unchanged.

## Current scope

Initial framework: client-only loading, a Right Shift toggle menu, an enabled-module
HUD, local dot commands, and persistent key bindings. Five modules have new source
implementations: Sprint, AutoWalk, AutoRespawn, AutoLog, and CrystalLog.

**No Java compilation or Minecraft runtime verification has completed.** Remaining
modules are listed in `PORTING.md`; they are not represented by fake menu toggles.
The menu is an initial replacement, not a recreation of the original draggable GUI.

## Build on Windows

Install a Java 21 JDK, extract/open this project, then open PowerShell in this folder:

```powershell
java -version
.\gradlew.bat clean build
```

The first build downloads Gradle, Forge and Minecraft dependencies and needs internet
access. Compilation may expose further API errors that must be fixed before use.
After a successful build, the development JAR is in `build/libs`.
To test in a development Minecraft client:

```powershell
.\gradlew.bat runClient
```

Linux/macOS equivalents: `bash gradlew clean build` and `bash gradlew runClient`.
A repository workflow in `../.github/workflows/forge-1.21.10.yml` also builds with
Java 21 and uploads a JAR only if compilation succeeds. It has not been run.

## Controls and state

- Right Shift opens the menu while in a world. Escape/Done closes it.
- `.help`, `.gui`, `.mods`, `.toggle Sprint`, `.alloff` run locally.
- `.bind Sprint R` assigns a key; `.bind Sprint NONE` clears it.
- Key bindings are stored in `config/xdolf.properties`.
- Modules start disabled and are disabled again on world changes.
- Opening screens suspends module actions; AutoRespawn may act on the death screen.
- AutoLog disconnects at 6 health; CrystalLog disconnects within 6 blocks of a crystal.

Binding a module to a vanilla key currently allows both actions. Per-module settings,
key capture in the menu, and the original friends/macros/waypoints system still need porting.

## Verification status

Inspected the original source, the official Forge 60.1.0 MDK, Forge 1.21.10 source
and EventBus 7 sources. The local Java 21 build initially failed resolving
`services.gradle.org`. After downloading Gradle separately, dependency resolution
failed on the Foojay toolchain resolver plugin before `compileJava` could run.
No successful compilation, launch, multiplayer test, or final JAR is claimed.

Preserves the original GPLv3 license and attribution to x0XP, Sgt Pepper, and the
original Xdolf contributors. The build includes only this project's source and
resources, not the old bundled Minecraft or shader source.
