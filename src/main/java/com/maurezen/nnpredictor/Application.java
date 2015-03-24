package com.maurezen.nnpredictor;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.maurezen.nnpredictor.model.Session;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Slf4j
public class Application {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) {
        try {
            Map<String, List<Session>> sessionMap = getSessions(Application.class.getResourceAsStream("/sample.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<Session>> getSessions(InputStream sample) throws IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(sample));

        Map<String, List<Session>> map = Maps.newHashMap();

        for (String[] row: reader.readAll()) {
            try {
                String id = row[4];
                List<Session> list = map.get(id);
                if (list == null) {
                    list = Lists.newArrayList();
                    map.put(id, list);
                }
                list.add(
                    new Session(
                        DATE_FORMAT.parse(row[0])
                        , DATE_FORMAT.parse(row[1])
                        , seconds(TIME_FORMAT.parse(row[2]))
                        , Double.parseDouble(row[3])
                        , id
                    )
                );
            } catch (ParseException e) {
                log.warn("Error parsing row: " + Joiner.on(' ').join(row));
            }
        }

        sortByStart(map);
        validate(map);
        return map;
    }

    /**
     * Some validation would be in order at some point, though not now.
     * @param map a map of session lists to be validated
     */
    private static void validate(Map<String, List<Session>> map) {
    }

    private static void sortByStart(Map<String, List<Session>> map) {
        for (List<Session> list : map.values()) {
            Collections.sort(
                list, (s1, s2) -> s1.getStart().compareTo(s2.getStart())
            );
        }

    }

    private static long seconds(@NotNull Date date) {
        return date.getSeconds() + 60*date.getMinutes() + 60*60*date.getHours();
    }
}
