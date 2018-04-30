package cl.mti.tesina.cleanseload.workers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import cl.mti.tesina.cleanseload.entities.Noticias;
import cl.mti.tesina.cleanseload.repositories.NoticiasRepository;
import cl.mti.tesina.cleanseload.services.CleanseService;

@Component("worker")
@Scope("prototype")
public class Worker implements Runnable
{
	private static final Logger log = LoggerFactory.getLogger(CleanseLoadNoticias.class);

	@Autowired
	private NoticiasRepository noticiasRepository;

	@Autowired
	private CleanseService cleanseService;
	private Integer numeroPagina;
	private int tamanoPagina = 1000;

	@Override
	public void run()
	{
		PageRequest pageRequest = PageRequest.of(numeroPagina, tamanoPagina);
		Page<Noticias> paginaNoticias = noticiasRepository.findAll(pageRequest);

		log.info("Pagina: " + paginaNoticias.getNumber() + " de " + paginaNoticias.getTotalPages());

		List<Noticias> listadoGrabar = new ArrayList<Noticias>();

		for (Noticias n : paginaNoticias)
		{
			if (n.getTexto() == null)
			{
				continue;
			}
			try
			{
				if (n.getTextoProcesado() == null)
				{
					String textoProcesado = cleanseService.cleanse(n.getTexto());
					n.setTextoProcesado(textoProcesado);
					listadoGrabar.add(n);
				}
			}
			catch (Exception ex)
			{
				log.error("Error con la noticia: " + n.getId());
			}

		}

		if (listadoGrabar.size() > 0)
		{
			noticiasRepository.saveAll(listadoGrabar);
		}

		log.info("Pagina " + numeroPagina + " Procesada");

	}

	public int getTamanoPagina()
	{
		return tamanoPagina;
	}

	public void setTamanoPagina(int tamanoPagina)
	{
		this.tamanoPagina = tamanoPagina;
	}

	public Integer getNumeroPagina()
	{
		return numeroPagina;
	}

	public void setNumeroPagina(Integer numeroPagina)
	{
		this.numeroPagina = numeroPagina;
	}

}
