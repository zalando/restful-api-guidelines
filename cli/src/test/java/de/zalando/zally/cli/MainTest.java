package de.zalando.zally.cli;

import com.github.ryenus.rop.OptionParser;
import org.junit.Test;


public class MainTest {
    @Test
    public void implementsRunMethod() {
        OptionParser optionParser = new OptionParser(Main.class);
        String[] args = new String[0];
        optionParser.parse(args);
    }
}