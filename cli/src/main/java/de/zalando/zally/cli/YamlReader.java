package de.zalando.zally.cli;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.Reader;
import java.util.Map;

public class YamlReader implements SpecsReader {

    private final Reader reader;

    public YamlReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public JsonValue read() throws RuntimeException {
        Map<String, Object> yaml = (Map<String, Object>) new Yaml().load(reader);
        return Json.parse(new JSONObject(yaml).toString());
    }
}
