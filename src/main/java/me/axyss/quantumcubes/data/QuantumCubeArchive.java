package me.axyss.quantumcubes.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuantumCubeArchive {
    private static final HashMap<String, QuantumCube> lastPlacedQuantumCube = new HashMap<>();

    public static void insert(UUID playerWhoPlaced, QuantumCube placedQuantumCube) {
        lastPlacedQuantumCube.put(playerWhoPlaced.toString(), placedQuantumCube);
    }

    public static QuantumCube extractLastPlacedBy(UUID player) {
        return lastPlacedQuantumCube.get(player.toString());
    }
}
