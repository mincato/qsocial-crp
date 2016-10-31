package com.qsocialnow.retroactiveprocess.service;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;

import com.odatech.microservices.request.RealTimeReportBean;
import com.odatech.microservices.response.EventsPaginatedResponse;

public class MockEventsRetroactiveService implements EventsRetroactiveService {

    @Override
    public EventsPaginatedResponse buildResponse(RealTimeReportBean request) throws Throwable {
        URI systemResource = ClassLoader.getSystemResource("mock/events").toURI();
        final Map<String, String> env = new HashMap<>();
        final String[] array = systemResource.toString().split("!");
        final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env);
        try {
            Thread.sleep(10000);
            if ("1500".equals(request.getScrollId())) {
                throw new Exception("Error de conexion");
            }
            final Path baseDir = fs.getPath(array[1]);

            final List<Path> entries;

            Integer offset = request.getScrollId() == null ? 0 : Integer.parseInt(request.getScrollId());

            final BiPredicate<Path, BasicFileAttributes> predicate = (path, attrs) -> attrs.isRegularFile();

            try (final Stream<Path> stream = Files.find(baseDir, 1, predicate);) {
                entries = stream.skip(offset).limit(request.getMaxResults()).collect(Collectors.toList());
            }
            EventsPaginatedResponse response = new EventsPaginatedResponse();
            String scrollId = null;
            List<String> events = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(entries)) {
                scrollId = String.valueOf(offset + request.getMaxResults());
                for (Path path : entries) {
                    events.add(Files.readAllLines(path).stream().collect(Collectors.joining()));
                }
            }
            response.setEvents(events);
            response.setScrollId(scrollId);
            return response;
        } finally {
            fs.close();

        }
    }

}
