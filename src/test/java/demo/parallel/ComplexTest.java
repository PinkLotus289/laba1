package test.java.demo.parallel;

import demo.parallel.Complex;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexTest {

    private static double re(Complex z) {
        try {
            Field f = Complex.class.getDeclaredField("re");
            f.setAccessible(true);
            return f.getDouble(z);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static double im(Complex z) {
        try {
            Field f = Complex.class.getDeclaredField("im");
            f.setAccessible(true);
            return f.getDouble(z);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void assertComplexCloseTo(Complex actual, double expRe, double expIm, double eps) {
        assertEquals(expRe, re(actual), eps, "real part");
        assertEquals(expIm, im(actual), eps, "imag part");
    }

    @Test
    void testPlus_MutatingAndChaining() {
        Complex a = new Complex(2, 3);
        Complex b = new Complex(-1, 4);

        Complex r = a.plus(b);               // a := a + b
        assertSame(a, r, "plus() should return this for chaining");
        assertComplexCloseTo(a, 1, 7, 1e-12);
    }

    @Test
    void testTimesComplex() {
        Complex a = new Complex(2, 3);
        Complex b = new Complex(-1, 4);

        a.times(b); // (2+3i)*(-1+4i) = (-14 + 5i)
        assertComplexCloseTo(a, -14, 5, 1e-12);
    }

    @Test
    void testMinus() {
        Complex a = new Complex(2, 3);
        Complex b = new Complex(-1, 4);

        a.minus(b); // (2+3i) - (-1+4i) = (3 - 1i)
        assertComplexCloseTo(a, 3, -1, 1e-12);
    }

    @Test
    void testTimesScalar() {
        Complex a = new Complex(2, 3);
        a.times(2.5); // (5, 7.5)
        assertComplexCloseTo(a, 5, 7.5, 1e-12);
    }

    @Test
    void testConjugate_Involution() {
        Complex a = new Complex(3, -2);
        a.conjugate(); // (3, 2)
        assertComplexCloseTo(a, 3, 2, 1e-12);

        a.conjugate(); // снова (3, -2)
        assertComplexCloseTo(a, 3, -2, 1e-12);
    }

    @Test
    void testDivide() {
        Complex a = new Complex(3, -2);
        Complex b = new Complex(1, 2);

        a.divide(b); // (3-2i)/(1+2i) = (-0.2, -1.6)
        assertComplexCloseTo(a, -0.2, -1.6, 1e-12);
    }

    @Test
    void testDivideByZero_NoChange() {
        Complex a = new Complex(1.5, -0.5);
        Complex before = a.copy();
        a.divide(new Complex(0, 0)); // по твоей реализации — без изменений
        assertComplexCloseTo(a, re(before), im(before), 0.0);
    }

    @Test
    void testCopyIndependence() {
        Complex a = new Complex(1, 2);
        Complex c = a.copy();

        c.plus(new Complex(1, 0)); // меняем копию
        assertComplexCloseTo(a, 1, 2, 0.0); // исходник не изменился
    }

    @Test
    void testLengthSq() {
        Complex a = new Complex(3, 4);
        assertEquals(25.0, a.lengthSQ(), 1e-12);
    }

    @Test
    void testFluentChain() {
        // ((1+1i) + (1-1i)) * (0+1i) = (2+0i)*(0+1i) = (0+2i)
        Complex r = new Complex(1, 1)
                .plus(new Complex(1, -1))
                .times(new Complex(0, 1));
        assertComplexCloseTo(r, 0, 2, 1e-12);
    }
}
