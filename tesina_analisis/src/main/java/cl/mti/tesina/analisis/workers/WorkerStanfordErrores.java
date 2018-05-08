package cl.mti.tesina.analisis.workers;

import java.util.Optional;

import org.python.jline.internal.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cl.mti.tesina.analisis.dto.CodigosResultados;
import cl.mti.tesina.analisis.dto.Estadisticas;
import cl.mti.tesina.analisis.dto.Idiomas;
import cl.mti.tesina.analisis.dto.ResultadoSentimiento;
import cl.mti.tesina.analisis.entities.Errores;
import cl.mti.tesina.analisis.entities.Noticias;
import cl.mti.tesina.analisis.entities.Sentimientos;
import cl.mti.tesina.analisis.repositories.ErroresRepository;
import cl.mti.tesina.analisis.repositories.NoticiasRepository;
import cl.mti.tesina.analisis.repositories.SentimientosRepository;
import cl.mti.tesina.analisis.repositories.StocksRepository;
import cl.mti.tesina.analisis.services.AnalizadorSentimientosService;

@Component("workerStanfordErrores")
@Scope("prototype")
public class WorkerStanfordErrores implements Runnable
{
	@Autowired
	@Qualifier("stanfordNlpSentimientosService")
	private AnalizadorSentimientosService analizadorSentimientosService;
	@Autowired
	private NoticiasRepository noticiasRepository;
	@Autowired
	private SentimientosRepository sentimientosRepository;
	@Autowired
	private ErroresRepository erroresRepository;
	@Autowired
	private StocksRepository stocksRepository;

	private Estadisticas estadisticas;
	private volatile int idNoticia;

	@Override
	public void run()
	{
		estadisticas.incrementLeidos();
		Optional<Noticias> n = noticiasRepository.findById(idNoticia);
		if (n.isPresent())
		{
			procesar(n.get());
		}

	}

	private void procesar(Noticias n)
	{
		ResultadoSentimiento resultado = analizadorSentimientosService.procesar(n.getTexto(), Idiomas.INGLES);

		if (resultado.getCodigoResultado() == CodigosResultados.OK)
		{
			Sentimientos s = new Sentimientos();

			s.setIdMotor(analizadorSentimientosService.getIdMotor());
			s.setNoticia(n);
			s.setSentimiento(resultado.getSentimiento());
			s.setPuntajePositivo(resultado.getPositivo());
			s.setPuntajeNegativo(resultado.getNegativo());
			s.setPuntajeNeutral(resultado.getNeutral());
			s.setPuntajeMixto(resultado.getMixto());
			sentimientosRepository.save(s);

			estadisticas.incrementGrabados();
		}
		else
		{
			Log.error("Error Procesando Noticia: " + idNoticia);
		}

	}

	public Estadisticas getEstadisticas()
	{
		return estadisticas;
	}

	public void setEstadisticas(Estadisticas estadisticas)
	{
		this.estadisticas = estadisticas;
	}

	public int getIdNoticia()
	{
		return idNoticia;
	}

	public void setIdNoticia(int idNoticia)
	{
		this.idNoticia = idNoticia;
	}

	
}
