package net.comorevi.nukkit.cosse.level;

import cn.nukkit.level.generator.Generator;

import net.comorevi.nukkit.cosse.AutomaticTasks;
import net.comorevi.nukkit.cosse.level.generator.WorldGenForest;

public class MainGenerator {

    AutomaticTasks plugin;

    public MainGenerator (AutomaticTasks plugin) {
        this.plugin = plugin;
        Generator.addGenerator(WorldGenForest.getGenerator("GEN_FOREST"), "GEN_FOREST", 0);
    }

}