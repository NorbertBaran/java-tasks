package uj.java.pwj2019.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Calc {

    BigDecimal calculate(String start, String end, BigDecimal dailyRate) throws IllegalArgumentException {
        //TODO implement
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm VV");
        ZonedDateTime from=ZonedDateTime.parse(start, dateTimeFormatter);
        ZonedDateTime to=ZonedDateTime.parse(end, dateTimeFormatter);
        Duration duration=Duration.between(from, to);

        if(duration.toMinutes()<=0)
            return BigDecimal.valueOf(0, 2);

        BigDecimal salary=new BigDecimal(0);
        BigDecimal days=new BigDecimal(duration.toDays());
        BigDecimal hours=new BigDecimal(duration.minusDays(duration.toDays()).toHours());
        BigDecimal minutes=new BigDecimal(duration.minusHours(duration.toHours()).toMinutes());

        if(minutes.compareTo(BigDecimal.ZERO)>0)
            hours=hours.add(BigDecimal.ONE);

        salary=salary.add(days.multiply(dailyRate));

        if(hours.compareTo(BigDecimal.ZERO) > 0){
            if(hours.compareTo(BigDecimal.valueOf(8))<=0)
                salary=salary.add(dailyRate.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP));
            else if(hours.compareTo(BigDecimal.valueOf(12))<=0)
                salary=salary.add(dailyRate.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
            else
                salary=salary.add(dailyRate);
        }

        return salary;
    }

}
