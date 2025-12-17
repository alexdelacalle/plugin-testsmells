package es.upm.grise.profundizacion;

import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;


public class CuentaBancariaTest {

    String numeroCuenta;
    double saldoInicial;
    boolean admiteDescubierto;
    List<Operacion> operaciones;
    CuentaBancaria cuenta;
    Operacion operacion;

    @Before
    public void setUp(){
        numeroCuenta = "ES1234567890";
        saldoInicial = 1000.00;
        cuenta = new CuentaBancaria(numeroCuenta, saldoInicial);
        operacion = mock(Operacion.class);
        when(operacion.getId()).thenReturn(1L);
        when(operacion.getImporte()).thenReturn(200.00);
        cuenta.addOperacion(operacion);
    }
    
    @Test
    public void testAddOperacionValida() {
        Operacion nuevaOperacion = mock(Operacion.class);
        when(nuevaOperacion.getId()).thenReturn(2L);
        try{
            cuenta.addOperacion(nuevaOperacion);

        }catch(OperacionNulaException | OperacionDuplicadaException e){
            fail("No se esperaba una excepcion");
        }

        assertEquals(2,cuenta.operaciones.size());
        
    }

    @Test(expected = OperacionNulaException.class)
    public void testAddOperacionNula() throws OperacionNulaException {
        cuenta.addOperacion(null);
    }

    @Test(expected = OperacionDuplicadaException.class)
    public void testAddOperacionDuplicada() throws OperacionDuplicadaException {
        cuenta.addOperacion(operacion);
    }

    @Test
    public void testGetSaldoActualValido() {
        try{
            double saldoActual = cuenta.getSaldoActual();
            assertEquals(1200.00, saldoActual, 0.001);
            Operacion operacion2 = mock(Operacion.class);
            when(operacion2.getId()).thenReturn(3L);
            when(operacion2.getImporte()).thenReturn(-1300.00);
            assertEquals(0.00, saldoActual -1200.00, 0.001);
        }catch(SaldoNegativoException e){
            fail("No se esperaba una excepcion");
        }
    }

   
}
