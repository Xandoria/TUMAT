package de.canitzp.tumat.integration;

import de.canitzp.tumat.api.IWorldRenderer;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public class EnergyColors implements IWorldRenderer{

    @Override
    public boolean shouldBeActive(){
        return false;
    }

    @Nonnull
    @Override
    public Map<String, String> getEnergyColor(){
        Map<String, String> map = new HashMap<>();
        map.put("refinedstorage", "RS");
        return map;
    }

}