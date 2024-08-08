package com.thuctap.quanlychungcu.utils;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
public class TimeTool {
    public static Timestamp getNow(){
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public static Timestamp convertToUTC(Timestamp time){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.minusHours(7);//trừ 7 tiếng
        return Timestamp.valueOf(localDateTime);
    }

    public static Timestamp plusDay(Timestamp time, int day){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.plusDays(day);
        return Timestamp.valueOf(localDateTime);
    }

    public static Timestamp minusDay(Timestamp time, int day){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.minusDays(day);
        return Timestamp.valueOf(localDateTime);
    }
    
    public static Timestamp plusSeconds(Timestamp time, int seconds){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.plusSeconds(seconds);
        return Timestamp.valueOf(localDateTime);
    }

    public static Timestamp minusSeconds(Timestamp time, int seconds){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.minusSeconds(seconds);
        return Timestamp.valueOf(localDateTime);
    }
}
