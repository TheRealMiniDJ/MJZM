# MJZM

A client-side Fabric mod for Minecraft Java Edition **1.21.1**.

## What it does

Adds a **MJZM** button to the top-right of the title screen. Clicking it starts a connection to:

`mjzm.minehost.gg`

## Build

The GitHub Actions workflow builds the mod automatically when code is pushed. Open the repository's **Actions** tab, select a successful **Build MJZM** run, and download the `MJZM-Fabric-1.21.1` artifact. The ZIP contains the built JAR.

For a local build, install JDK 21 and Gradle 8.8, then run:

```sh
gradle build
```

The JAR will be in `build/libs`.

## Install

Put the built JAR in the Minecraft instance's `mods` folder with Fabric Loader for Minecraft 1.21.1 installed.

This mod only adds the shortcut button. It does not change Minecraft account authentication or server access rules. The server may require a properly authenticated account.


Build verification triggered after the screen accessor fix.
