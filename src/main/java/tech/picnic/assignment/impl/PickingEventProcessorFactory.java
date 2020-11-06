package tech.picnic.assignment.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.auto.service.AutoService;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.picnic.assignment.api.EventProcessorFactory;
import tech.picnic.assignment.api.StreamProcessor;
import tech.picnic.assignment.impl.model.input.Pick;
import tech.picnic.assignment.impl.model.input.Picker;
import tech.picnic.assignment.impl.model.input.TemperatureZoneEnum;
import tech.picnic.assignment.impl.model.output.PickResponse;
import tech.picnic.assignment.impl.model.output.PickerResponse;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;
import static tech.picnic.assignment.util.JobUtils.runJobWithTimeOut;

@AutoService(EventProcessorFactory.class)
public final class PickingEventProcessorFactory implements EventProcessorFactory {

    private final Logger log = LoggerFactory.getLogger(PickingEventProcessorFactory.class);

    private static final TemperatureZoneEnum TEMPERATURE_ZONE_FILTER = TemperatureZoneEnum.ambient;

    private JsonFactory jsonFactory = new JsonFactory();
    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private Map<Picker, List<Pick>> pickersTreeMap = new TreeMap<>();

    static {
        // LOG4J simple configurator
        BasicConfigurator.configure();
    }

    //region public methods
    @Override
    public StreamProcessor createProcessor(int maxEvents, Duration maxTime) {
        return (source, sink) -> {
            // READING
            log.debug("maxEvents = {}, maxTime = {} sec", maxEvents, maxTime.getSeconds());
            long startTime = System.currentTimeMillis();
            runJobWithTimeOut(() -> readPicks(source, maxEvents, TEMPERATURE_ZONE_FILTER), maxTime);
            long duration = (System.currentTimeMillis() - startTime);

            Integer totalPickedEvents = pickersTreeMap.values().stream().mapToInt(Collection::size).sum();
            log.info("Fetch {} different pickers(s) with total {} pick(s) in {} ms", pickersTreeMap.size(), totalPickedEvents, duration);

            // PROCESSING
            List<PickerResponse> pickerResponses = mapTreePicksToPickerResponses(pickersTreeMap);

            // WRITING
            sink.write(objectMapper.writeValueAsBytes(pickerResponses));
        };
    }
    //endregion public methods

    //region private methods
    private void readPicks(InputStream source, int maxEvents, TemperatureZoneEnum temperatureZone) {
        try (JsonParser parser = jsonFactory.createParser(source)) {
            parser.setCodec(objectMapper);
            parser.nextToken();

            int eventCount = 0;
            while (parser.hasCurrentToken() && maxEvents > eventCount) {
                Pick pick = parser.readValueAs(Pick.class);
                if (pick.getArticle().getTemperature_zone() == temperatureZone) {
                    addPick(pick);
                }
                eventCount++;
                parser.nextToken();
            }
            log.debug("Read {} event(s)", eventCount);
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    private void addPick(Pick pick) {
        // Group by pickers and sorted by active_since
        if (pickersTreeMap.containsKey(pick.getPicker())) {
            pickersTreeMap.get(pick.getPicker()).add(pick);
        } else {
            List<Pick> newPickList = new ArrayList<>();
            newPickList.add(pick);
            pickersTreeMap.put(pick.getPicker(), newPickList);
        }
    }

    private static List<PickerResponse> mapTreePicksToPickerResponses(Map<Picker, List<Pick>> picks) {
        return picks.entrySet().stream()
                .map(picker -> new PickerResponse(
                                picker.getKey().getName(),
                                picker.getKey().getActive_since(),
                                picker.getValue().stream()
                                        .map(pick -> new PickResponse(
                                                pick.getArticle().getName().toUpperCase(),
                                                pick.getTimestamp()))
                                        .sorted(Comparator.comparing(PickResponse::getTimestamp))
                                        .collect(toList())
                        )
                )
                .collect(toList());
    }
    //endregion private methods
}