package com.joenastan.sleepingwars.game.ItemPrice;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PricesUpgradeItems extends PricesItems {

    public PricesUpgradeItems(@Nonnull Material item, @Nonnull Material currency, @Nonnull String itemName,
                              int price, int defaultAmountGetter, @Nullable List<String> lore) {
        super(item, currency, itemName, price, defaultAmountGetter, lore);
    }

}
