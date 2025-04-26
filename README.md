<p align="center">
    A Minecraft Server Plugin
</p>
<p align="center">
  <img src="https://cdn.modrinth.com/data/ToCzxMGc/1755362652147cec534e693a26b1f594f3a12f26_96.webp" alt="Logo">
  <h1 align="center">Geyser Recipe Fix</h1>
</p>

<p align="center">
  Allows Bedrock (Geyser) Players to use custom anvil and smithing recipes.
</p>

Allows Bedrock (Geyser) Players to use custom anvil and smithing recipes with custom menus.

#### This plugin will:

- Add a geyser texture pack (1 custom item)
- Override the anvil and smithing table menus for **ONLY** bedrock players (Java Players are not affected)
- Use packets so most any other plugin or datapack will work with it

#### installation:

1. Make sure [Geyser](https://geysermc.org/download) and/or [floodgate](https://geysermc.org/download#floodgate) is in your plugins
2. Download and install [packetevents](https://modrinth.com/plugin/packetevents) <small>(use [ProtocolLib](https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/) for v1.4.1 and below)</small>
3. Download and place in the plugins folder <small>(This is **NOT** a geyser extension, It's a separate plugin that hooks into geyser)</small>
4. Restart/Start server

<details>
  <summary>Proxy setup</summary>
  
1. Install Geyser on proxy
2. Install floodgate on servers behind proxy and proxy [(more info, under "Proxy Servers" tab)](https://wiki.geysermc.org/floodgate/setup/)
3. Install this plugin on servers behind the proxy (with [packetevents](https://modrinth.com/plugin/packetevents) <small> or [ProtocolLib](https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/) for v1.4.1 and below</small>)
4. Run servers to generate files/directories
5. Move "GeyserRecipeFix-Mapping.json" (Located in this plugin's folder) to Geyser's "custom-mappings" folder
6. Move "GeyserRecipeFix-Pack.mcpack" (same place) to Geyser's "packs" folder
</details>

Note for players:
In the anvil menu, you can click the anvil in the corner to get the default menu (Useful for renaming)

© Sideways-Sky, 2023, All Rights Reserved
