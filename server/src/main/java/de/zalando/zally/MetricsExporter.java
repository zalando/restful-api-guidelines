package de.zalando.zally;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MetricsExporter implements ApplicationListener<ApplicationReadyEvent> {

    private final MetricRegistry metricRegistry;

    @Autowired
    public MetricsExporter(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        final Slf4jReporter reporter = Slf4jReporter.forRegistry(metricRegistry)
            .outputTo(LoggerFactory.getLogger(MetricsExporter.class))
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build();
        reporter.start(1, TimeUnit.MINUTES);
    }
}
