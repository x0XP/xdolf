# Port coverage and next steps

Original source commit: `6d0de4589cc8475380113aee0bdaa2ab4280feef`.

**This is an incomplete port. Every new implementation is uncompiled and untested in Minecraft.**

| Original enabled module | Current state |
| --- | --- |
| AutoFish | Not ported |
| Fullbright | Not ported |
| Tracers | Not ported |
| StorageESP | Not ported |
| EntityESP | Not ported |
| NoHurtCam | Not ported |
| AntiVelocity | Not ported |
| Flight | Not ported |
| Spammer | Not ported |
| Timer | Not ported |
| XRay | Not ported |
| KillAura | Not ported |
| AutoRespawn | New source implementation; compile/runtime verification blocked |
| AutoArmor | Not ported |
| AutoWalk | New source implementation; compile/runtime verification blocked |
| Chams | Not ported |
| GUI | Initial replacement menu; original layout not ported |
| SafeWalk | Not ported |
| AutoLog | New source implementation; compile/runtime verification blocked |
| NoSlowdown | Not ported |
| FastPlace | Not ported |
| HorseJump | Not ported |
| Sprint | New source implementation; compile/runtime verification blocked |
| Trajectories | Not ported |
| CrystalAura | Not ported |
| Freecam | Not ported |
| Nametags | Not ported |
| Criticals | Not ported |
| NoFall | Not ported |
| CrystalLog | New source implementation; compile/runtime verification blocked |
| AntiHunger | Not ported |
| AutoEat | Not ported |
| Jesus | Not ported |
| EntitySpeed | Not ported |
| Speedmine | Not ported |
| EntityStep | Not ported |
| ElytraFly | Not ported |
| ElytraPlus | Not ported |

38 modules are registered by the old client. Waypoints and AutoTotem are commented out there; they have not been ported either.

## Build blocker

Java 21 and Gradle 8.12.1 were obtained. The build stops resolving the Foojay 0.10.0 Gradle settings plugin, before compiling source. The wrapper first failed with UnknownHostException for services.gradle.org. Downloading Gradle separately overcame that download only; the dependency resolution problem remains. No game/API compile success is implied by source inspection.

## Next work, in order

1. Run the included GitHub workflow or a Java 21 build on a machine with working dependency access; fix compile errors.
2. Launch 1.21.10 with Forge 60.1.0. Check menu/HUD, normal chat versus dot commands, keybind persistence, screen suspension, death/respawn, disconnect and world changes.
3. Rebuild options, friends, macros, draggable category windows and the remaining local commands.
4. Port inventory automation against modern data components and menu transactions; add fishing event hooks.
5. Port remaining movement/network hooks individually; old 1.12.2 packet behavior cannot be assumed valid for 1.21.10.
6. Rebuild ESP/tracers/nametags/trajectories/XRay rendering against 1.21.10 rendering APIs. Do not copy old fixed-function OpenGL or bundled shader/Minecraft classes.
7. Validate every listed module in game before calling the full client port complete.

No repository changes have been pushed and no GitHub Actions workflow has run.
