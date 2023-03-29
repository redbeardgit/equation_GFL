package equation_cheking;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EquationCreatorTest {
    private final EquationCreator tester = new EquationCreator();

    @Test
    void isDigit() {
        assertTrue(tester.isDigit("23"));
        assertFalse(tester.isDigit("we"));
    }

    @Test
    void checkBrackets() {
        assertTrue(tester.checkBrackets("(28*(6-1))"));
        assertFalse(tester.checkBrackets("(6+(3+("));
    }

    @Test
    void checkEquation() {
        ArrayList<String> arr = new ArrayList<String>(Arrays.asList("-1.3", "*", "(", "-5", "/", "x", ")"));
        assertTrue(tester.checkEquation("-1.3*(-5/x)"));
        assertEquals(arr, tester.getCorrect_equation());
        assertFalse(tester.checkEquation("1.3*5/-x"));
        assertFalse(tester.checkEquation("1.3*a/x"));
        assertFalse(tester.checkEquation("2+*4"));
        arr = new ArrayList<String>(Arrays.asList("(", "26.1", "+", "15", ")", "*", "-5", "/", "10", "+", "x"));
        assertTrue(tester.checkEquation("(26.1+15)*-5/10+x"));
        assertEquals(arr, tester.getCorrect_equation());

    }
}