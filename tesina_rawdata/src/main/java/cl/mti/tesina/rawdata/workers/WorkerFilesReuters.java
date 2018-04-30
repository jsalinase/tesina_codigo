package cl.mti.tesina.rawdata.workers;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import cl.mti.tesina.rawdata.entities.Noticias;
import cl.mti.tesina.rawdata.services.NoticiasService;

//@Component
@ConfigurationProperties(prefix = "tesina")
public class WorkerFilesReuters implements CommandLineRunner
{

	private static final Logger log = LoggerFactory.getLogger(WorkerFilesReuters.class);

	private String rutaArchivosReuters;

	@Autowired
	private NoticiasService noticiasService;

	@Override
	public void run(String... args) throws Exception
	{
		log.info("Procesando Noticias Reuters");
		File directorioArchivosReuters = new File(rutaArchivosReuters);

		List<Noticias> listadoGrabar = new ArrayList<Noticias>();
		if (directorioArchivosReuters.exists())
		{
			int leidos = 0;
			int grabados = 0;
			SimpleDateFormat spf = new SimpleDateFormat("yyyyMMdd");
			for (File f : directorioArchivosReuters.listFiles())
			{
				Date fechaDirectorio = null;
				if (f.isDirectory())
				{
					fechaDirectorio = spf.parse(f.getName());

					for (File fNoticia : f.listFiles())
					{
						try
						{
							if (fNoticia.length() == 0)
							{
								continue;
							}

							leidos++;

							if (leidos % 10000 == 0)
							{
								log.info("Leidos: " + leidos + ". Grabados: " + grabados);
							}

							Noticias n = new Noticias();
							n.setFecha(fechaDirectorio);
							n.setRecurso("");
							try (BufferedReader r = Files.newBufferedReader(Paths.get(fNoticia.getPath()),
									Charset.defaultCharset()))
							{
								StringBuilder b = new StringBuilder();
								int i = 0;
								for (String s : r.lines().toArray(String[]::new))
								{
									if (s.startsWith("--"))
									{
										if (i == 0)
										{
											n.setTitulo(limpiarString(s));
										}
										if (i == 1)
										{
											n.setAutor(limpiarString(s));
										}
										if (i == 3)
										{
											n.setRecurso(limpiarString(s));
										}
									}
									else
									{
										b.append(limpiarString(s) + " ");
									}
									i++;
								}
								n.setTexto(b.toString().trim());
							}

							listadoGrabar.add(n);
							if (listadoGrabar.size() > 100)
							{
								noticiasService.grabarNoticiasReuters(listadoGrabar);
								listadoGrabar.clear();
								grabados += 100;
							}
						}
						catch (Exception ex)
						{
							log.error("Error procesando: " + fNoticia.getName());
						}
					}
				}
				else
				{
					continue;
				}
			}
			if (listadoGrabar.size() > 0)
			{
				noticiasService.grabarNoticiasReuters(listadoGrabar);
				log.info("Leidos: " + leidos + ". Grabados: " + grabados);
			}

		}
		else

		{
			log.error("No existe la ruta de noticias Reuters");
		}

	}

	public String limpiarString(String s)
	{
		if (s != null)
		{
			return s.replaceAll("--", "").trim();
		}
		else
		{
			return "";
		}
	}

	public String getRutaArchivosReuters()
	{
		return rutaArchivosReuters;
	}

	public void setRutaArchivosReuters(String rutaArchivosReuters)
	{
		this.rutaArchivosReuters = rutaArchivosReuters;
	}

}
