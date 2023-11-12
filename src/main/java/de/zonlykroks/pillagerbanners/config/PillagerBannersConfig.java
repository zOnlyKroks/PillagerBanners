package de.zonlykroks.pillagerbanners.config;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.ArrayList;
import java.util.List;

public class PillagerBannersConfig extends MidnightConfig {

    @Entry public static List<String> allowedEntities = new ArrayList<>();
    @Entry public static List<String> allowedItems = new ArrayList<>();
}
