import org.junit.Assert;
import org.junit.Test;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorTest {
    Calculator calculator = new Calculator();

    @Test
    public void sameTimeZone() {
        BigDecimal actual = calculator.calculate("2019-01-02 13:00 Europe/Warsaw", "2019-01-06 14:00 Europe/Warsaw", BigDecimal.valueOf(35.00));
        BigDecimal expected = BigDecimal.valueOf(151.67).setScale(2, RoundingMode.HALF_EVEN);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void differentTimeZone() {
        BigDecimal actual = calculator.calculate("2019-01-02 11:00 Europe/Warsaw", "2019-01-02 19:30 Europe/Kiev", BigDecimal.valueOf(30.00));
        BigDecimal expected = BigDecimal.valueOf(10.00).setScale(2, RoundingMode.HALF_EVEN);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void endOfDelegationEarlierThanStart() {
        BigDecimal actual = calculator.calculate("2019-01-02 00:07 Europe/Warsaw", "2019-01-02 00:07 Europe/Warsaw", BigDecimal.valueOf(38.00));
        BigDecimal expected = BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_EVEN);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void longTimeDelegation() {
        BigDecimal actual = calculator.calculate("2019-01-02 13:00 Europe/Warsaw", "2019-01-06 14:00 Europe/Berlin", BigDecimal.valueOf(49.00));
        BigDecimal expected = BigDecimal.valueOf(212.33).setScale(2, RoundingMode.HALF_EVEN);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shortTimeDelegation() {
        BigDecimal actual = calculator.calculate("2019-01-02 00:00 Europe/Warsaw", "2019-01-02 00:00 Europe/London", BigDecimal.valueOf(33.00));
        BigDecimal expected = BigDecimal.valueOf(11.00).setScale(2, RoundingMode.HALF_EVEN);
        Assert.assertEquals(expected, actual);
    }
}