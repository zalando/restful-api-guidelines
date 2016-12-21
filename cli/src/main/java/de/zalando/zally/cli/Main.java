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

    /**
     * Example for OAuth call to zally server:
     HttpResponse<String> response = Unirest.post(System.getenv("ZALLY_URL" ... or default))
         .header("Authorization", "Bearer " + System.getenv("TOKEN"))
         .asString();
     */
}
