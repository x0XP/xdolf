# Visual restoration checkpoint — dev.8, unbuilt

The user's requirement is the original appearance and behaviour, not a redesigned
UI or approximate replacements. These changes are local pending a successful
Forge build, world screenshots, and review against the reference client.

## Source-defined changes

| Area | Original reference and restoration |
| --- | --- |
| Click GUI | `clickgui/`: seven 100-unit draggable windows, original coordinates/order, translucent black, half-unit borders, red controls, centred names, pin/open state, option popups and 13 numeric sliders. |
| Font | `fonts/Fonts`, `XFont`, `XFontRenderer`: AWT Roboto 36 lookup, OS fallback, quarter-size glyphs, original spacing and shadow. The same atlas now serves world nametags. |
| Tracers | `mods/render/Tracers`: 1.5 physical-pixel lines to player feet; friend green, red within 6 blocks, red/yellow through 96, blue beyond 96; chest corner endpoints and green chest lines. Removed the replacement range/48-target/off-screen caps. |
| StorageESP | `StorageESP`, `RenderUtils.blockEsp`: translucent 25% fills and 50% black 1-pixel edges; green chests, red trapped chests, magenta ender chests, yellow shulkers, white furnace/dispenser/hopper; merged double chests and storage minecarts. |
| EntityESP | `EntityESP`, `RenderUtils.drawEntityESP`: original filters and outline mode; box mode restores 15% fills, black edges, expanded bounds, red enemies/monsters, blue friends, green passive/item entities. |
| Nametags | `Nametags`: players only, name plus green health percentage, blue friend name, original translucent black border, distance-relative world scale and sneaking offset; suppresses duplicate vanilla names. |
| Trajectories | `Trajectories`: 1.8-pixel line, original launch offset/gravity/charge equations, fishing rod support, distance colour and translucent landing cube instead of a GUI marker. |
| HUD | `gui/XDolfOverlay`: no replacement top-left watermark; right-aligned width-sorted white module names at 10-unit spacing; potion rows bottom-right; pinned panels; hidden during chat/debug. |

Geometry is now submitted through Forge's world frame graph, using the live world
projection and camera rotation. It is not clipped/projected into GUI coordinates.
Scene extraction produces immutable per-frame geometry; rendering consumes it.

## Reference gaps that must not be presented as verified parity

- `mods/render/Chams.java` contains only a constructor. No Chams render hook exists
  elsewhere in the supplied source. The prior functional textured-through-wall
  implementation is retained and restricted to players, matching the description;
  its exact appearance needs a working original client or screenshot reference.
- XRay's supplied source reads a user-owned `xray.txt`; that file is not in the
  repository. Its only located world hook changes chunk-visibility handling.
  The existing Forge ore/storage filter remains; an identical custom selection
  cannot be inferred from a missing configuration.
- NoHurtCam's supplied class lacks a render hook. The port's hurt-camera cancellation
  remains. Fullbright keeps its local night-vision rendering without changing real
  server potion effects.
- Potion icons and entity outline shaders currently come from Minecraft 1.21.10.
  The supplied repository contains no 1.12.2 icon assets. Font fallback also depends
  on the installed fonts. Pixel identity across versions/OS is not established.

## Validation and blocking issue

The GUI-only dev.7 commit `ac775233b2e0e4e14a1f5d1b61e33ddeafcf5c2f` compiled and
passed GUI interaction/state and world startup checks. Its framebuffer screenshot
was blank. A switch to Minecraft's screenshot API is staged with these changes.

Pure Java golden tests for the original tracer colour boundaries, line widths,
health-label format and sneaking offsets passed locally using Java 17's compiler
module. All 44 Java source files also passed a syntax-only parser check. Neither check
validates Minecraft APIs, mixin targets, rendering, or the full Forge compile.

The dev.8 world-render rewrite has NOT compiled or run. GitHub create-tree was
rejected by automatic approval review because the account reached a usage limit;
no indirect push was attempted. The local Maven dependency request was cancelled
by network approval. Therefore no updated installable JAR is claimed.

When publishing/build access resumes:

1. Compile dev.8, resolve exact Forge 60.1.0 API/mixin errors, and run the included
   golden tests and opt-in world/GUI tests.
2. The smoke test must prove the world pass actually rendered lines, boxes and tags
   (not merely that their modules were enabled). It captures fixed visual fixtures
   and the expanded GUI through Minecraft's screenshot API.
3. Inspect both PNGs; reject blank images, missing targets, incorrect colours,
   widths, fills, fonts, label duplication, camera/bobbing drift or GUI scaling.
4. Check off-screen/behind-camera targets, near clipping, multiple GUI scales,
   double/trapped chests, friend colours, damage/health labels, fishing rod/bow
   trajectories and both EntityESP modes in a running client.
5. Obtain the missing original Chams/XRay/icon reference if literal parity for those
   elements is required, and only then describe the result as visually identical.
