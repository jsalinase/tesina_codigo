package cl.mti.tesina.analisis.workers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import cl.mti.tesina.analisis.dto.CodigosResultados;
import cl.mti.tesina.analisis.dto.Estadisticas;
import cl.mti.tesina.analisis.dto.Idiomas;
import cl.mti.tesina.analisis.dto.ResultadoSentimiento;
import cl.mti.tesina.analisis.entities.Errores;
import cl.mti.tesina.analisis.entities.Noticias;
import cl.mti.tesina.analisis.entities.Sentimientos;
import cl.mti.tesina.analisis.entities.Stocks;
import cl.mti.tesina.analisis.repositories.ErroresRepository;
import cl.mti.tesina.analisis.repositories.NoticiasRepository;
import cl.mti.tesina.analisis.repositories.SentimientosRepository;
import cl.mti.tesina.analisis.repositories.StocksRepository;
import cl.mti.tesina.analisis.services.AnalizadorSentimientosService;

@Component("workerStanford")
public class WorkerStanford implements Runnable
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
	private int tamanoPagina;
	private int numeroPagina;

	@Override
	public void run()
	{
		PageRequest request = PageRequest.of(numeroPagina, tamanoPagina);

		Page<Stocks> paginaNoticias = stocksRepository.findAll(request);

		for (Stocks a : paginaNoticias)
		{
			estadisticas.incrementLeidos();
			Optional<Noticias> n = noticiasRepository.findById(a.getStocksId().getId());
			if (n.isPresent())
			{
				procesar(n.get());
			}
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
			try
			{
				Errores e = new Errores();
				e.setId(n.getId());
				erroresRepository.save(e);
				estadisticas.incrementErrores();
			}
			catch (Exception ex)
			{

			}
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

	public int getTamanoPagina()
	{
		return tamanoPagina;
	}

	public void setTamanoPagina(int tamanoPagina)
	{
		this.tamanoPagina = tamanoPagina;
	}

	public int getNumeroPagina()
	{
		return numeroPagina;
	}

	public void setNumeroPagina(int numeroPagina)
	{
		this.numeroPagina = numeroPagina;
	}

}
