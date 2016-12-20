package de.zalando.zally.cli;

import com.github.ryenus.rop.OptionParser;


@OptionParser.Command(name = "zally", descriptions = "Lints the given swagger file using Zally service")
public class Main {
    public static void main(String[] args) {
        OptionParser parser = new OptionParser(Main.class);
        parser.parse(args);
    }

    void run(String[] args) {
        // TODO: Implement command
    }
}
