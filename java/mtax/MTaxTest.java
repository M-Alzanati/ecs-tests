import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestJunit {
    MTax _mTax = new MTax();

    @Test
    public void testWithNoTaxes() {
        String err = _mTax.validate(null).get(0);
        assertEquals("El documento no tiene tasas", err);
    }

    @Test
    public void testWithEmptyTaxes() {
        String err = _mTax.validate(new List<X_Tax>()).get(0);
        assertEquals("El documento no tiene tasas", err);
    }

    @Test
    public void testWhenTaxNotExist() {
        X_Tax myTax = new X_Tax();

        List<X_Tax> myTaxes = new List<X_Tax>();
        myTaxes.add(myTax);

        String err = _mTax.validate(myTaxes).get(0);
        assertEquals("El impuesto es obligatorio", err);
    }

    @Test
    public void testWhenTaxIdNotExist() {
        X_Tax myTax = new X_Tax();

        List<X_Tax> myTaxes = new List<X_Tax>();
        myTax.setTax("123e5v");
        myTaxes.add(myTax);

        String err = _mTax.validate(myTaxes).get(0);
        assertEquals("El impuesto id es obligatorio", err);
    }

    @Test
    public void testWhenAmountNotExist() {
        X_Tax myTax = new X_Tax();

        List<X_Tax> myTaxes = new List<X_Tax>();
        myTax.setTax("123e5v");
        myTax.setId("eee5123");
        myTaxes.add(myTax);

        String err = _mTax.validate(myTaxes).get(0);
        assertEquals("El importe es obligatorio", err);
    }
}