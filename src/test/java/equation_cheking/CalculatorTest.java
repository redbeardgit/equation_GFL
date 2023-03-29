package equation_cheking;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void calculate() {
        ArrayList<String> arr = new ArrayList<String>(Arrays.asList("-1.3", "*", "(", "-5", "/", "2", ")"));
        Calculator calculator = new Calculator();
        assertEquals(3.25, calculator.calculate(arr));
        arr = new ArrayList<>(Arrays.asList("(", "26.1", "+", "15", ")", "*", "-5", "/", "10", "+", "10"));
        assertEquals(-10.55,calculator.calculate(arr));
    }
}