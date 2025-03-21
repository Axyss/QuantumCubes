<img style="z-index: 10;" src="https://raw.githubusercontent.com/Axyss/QuantumCubes/refs/heads/master/.github/assets/icon.png" alt="quantum_cubes_logo" align="right" width="150"></img>
# QuantumCubes
## What's this?

<p style="max-width: 90ch;">
    QuantumCubes is a Spigot plugin that provides users with an intuitive method for accessing the vast database of 
    <a href="https://minecraft-heads.com/">Minecraft Heads</a>
. It enhances the Minecraft experience by allowing players to obtain Quantum Cubes, special heads able to transform once into any of the more than 100K heads present in the website.
</p>

❗**ProtocolLib and Minecraft versions 1.20-1.21.4 are required**

## How to use
1. Go to <a href="https://minecraft-heads.com/">Minecraft Heads</a>
2. Choose a head and copy its ID
3. Place a Quantum Cube and introduce the previous ID on the first line of the sign GUI
4. Enjoy!

ℹ️ IDs are numeric values that can be found in the URL and other parts of the page such as:<br><br>
<img src="https://raw.githubusercontent.com/Axyss/QuantumCubes/refs/heads/master/.github/assets/ids.png" width="300px" text-align="right">

## Demonstration

<p align="center">
<img style="border: 2px solid white; border-radius: 60px;" src="https://raw.githubusercontent.com/Axyss/QuantumCubes/refs/heads/master/.github/assets/demo.gif" width="500px" text-align="right">
</p>

## Commands
- **/qc help** - Displays the list of plugin commands and their usages.
- **/qc give** \<player\> [amount] - Gives a certain player the selected amount of cubes.
- **/qc refresh** - Forces a manual refresh of the internal head database.
- **/qc reload** - Reloads the plugin's configuration.

## Permissions
| Permission node      | Description                               | Default setting |
|----------------------|-------------------------------------------|-----------------|
| quantumcubes.help    | Controls usage of the /qc help command    | OP              |
| quantumcubes.give    | Controls usage of the /qc give command    | OP              |
| quantumcubes.refresh | Controls usage of the /qc refresh command | OP              |
| quantumcubes.reload  | Controls usage of the /qc reload command  | OP              |
| quantumcubes.use     | Controls usage of the cubes themselves    | Everyone        |

## License
MIT license, do as you wish while you acknowledge my work :)
