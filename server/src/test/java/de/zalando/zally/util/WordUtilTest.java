package de.zalando.zally.util;

import org.junit.Test;

import static de.zalando.zally.util.WordUtil.isPlural;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WordUtilTest {
    @Test
    public void positiveCasePluralized() {
        assertTrue(isPlural("dogs"));
        assertTrue(isPlural("resources"));
        assertTrue(isPlural("payments"));
        assertTrue(isPlural("orders"));
        assertTrue(isPlural("parcels"));
        assertTrue(isPlural("commissions"));
        assertTrue(isPlural("commission_groups"));
        assertTrue(isPlural("articles"));
        assertTrue(isPlural("merchants"));
        assertTrue(isPlural("warehouse-locations"));
        assertTrue(isPlural("sales-channels"));
        assertTrue(isPlural("domains"));
        assertTrue(isPlural("addresses"));
        assertTrue(isPlural("bank-accounts"));

    }

    @Test
    public void negativeCasePluralized() {
        assertFalse(isPlural("cat"));
        assertFalse(isPlural("resource"));
        assertFalse(isPlural("payment"));
        assertFalse(isPlural("order"));
        assertFalse(isPlural("parcel"));
        assertFalse(isPlural("item"));
        assertFalse(isPlural("commission"));
        assertFalse(isPlural("commission_group"));
        assertFalse(isPlural("article"));
        assertFalse(isPlural("merchant"));
        assertFalse(isPlural("warehouse-location"));
        assertFalse(isPlural("sales-channel"));
        assertFalse(isPlural("domain"));
        assertFalse(isPlural("address"));
        assertFalse(isPlural("bank-account"));
    }

    @Test
    public void specialCasePluralized() {
        assertTrue(isPlural("vat")); //whitelisted
    }
}
