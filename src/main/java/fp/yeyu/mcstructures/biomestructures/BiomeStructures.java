package fp.yeyu.mcstructures.biomestructures;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BiomeStructures implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();
    @Override
    public void onInitialize() {
        LOGGER.info("Mod is loaded.");
    }
}
