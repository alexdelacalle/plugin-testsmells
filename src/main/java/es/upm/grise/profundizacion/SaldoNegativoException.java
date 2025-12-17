package es.upm.grise.profundizacion;

public class SaldoNegativoException extends RuntimeException {
    public SaldoNegativoException(String mensaje) {
        super(mensaje);
    }
}
