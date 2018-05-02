/**
} * Se debe implementar esta interfaz para hacer un Analizador de Sentimientos
 */
package cl.mti.tesina.analisis.services;

import cl.mti.tesina.analisis.dto.ResultadoSentimiento;


public interface AnalizadorSentimientosService
{
	ResultadoSentimiento procesar(String texto, String idioma);
	ResultadoSentimiento procesar(String texto);
	int getIdMotor();
}
