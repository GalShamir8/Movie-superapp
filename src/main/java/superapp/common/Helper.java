package superapp.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import superapp.data.CreationEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;


public class Helper {

    public static boolean validateEmail(String email){
        return Pattern.compile(Constants.EMAIL_REGEX).matcher(email).find();
    }

    public static String getSuperAppDomain(){
        return Constants.DOMAIN_URL.substring(1, Constants.DOMAIN_URL.length() - 1);
    }

    public static String generateUuid(String value) {
        return UUID.nameUUIDFromBytes(value.getBytes()).toString();
    }

    public static String mapToJson(ObjectMapper mapper, Map<String, ?> map){
        try {
            return mapper.writeValueAsString(map);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, ?> jsonToMap(ObjectMapper mapper, String jsonStr){
        try {
            return mapper.readValue(jsonStr, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Date> buildDateBetween(String creationEnum) {
        CreationEnum dateScopeCase = CreationEnum.valueOf(creationEnum);
        ArrayList<Date> result = new ArrayList<>();

        switch (dateScopeCase){
            case LAST_DAY -> extractLastDay(result);
            case LAST_HOUR -> extractLastHour(result);
            case LAST_MINUTE -> extractLastMinute(result);
        }

        return result;
    }

    private static void extractLastMinute(ArrayList<Date> result) {
        LocalDateTime now = LocalDateTime.now();
        result.add(localDateTimeToDate(now));
        result.add(localDateTimeToDate(now.minusMinutes(1)));
    }

    private static void extractLastHour(ArrayList<Date> result) {
        LocalDateTime now = LocalDateTime.now();
        result.add(localDateTimeToDate(now));
        result.add(localDateTimeToDate(now.minusHours(1)));
    }

    private static void extractLastDay(ArrayList<Date> result) {
        LocalDate today = LocalDate.now();
        result.add(localDateTimeToDate(today.atStartOfDay()));
        result.add(localDateTimeToDate(today.atTime(LocalTime.MAX)));
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
