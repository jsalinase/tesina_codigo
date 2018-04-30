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
public class WorkerFilesBloomberg implements CommandLineRunner
{

	private static final Logger log = LoggerFactory.getLogger(WorkerFilesBloomberg.class);

	private String rutaArchivosBloomberg;

	@Autowired
	private NoticiasService noticiasService;

	@Override
	public void run(String... args) throws Exception
	{
		log.info("Procesando Noticias Bloomberg");
		File directorioArchivosBloomberg = new File(rutaArchivosBloomberg);

		List<Noticias> listadoGrabar = new ArrayList<Noticias>();
		if (directorioArchivosBloomberg.exists())
		{
			int leidos = 0;
			int grabados = 0;
			SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
			for (File f : directorioArchivosBloomberg.listFiles())
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
							if (listadoGrabar.size() > 1000)
							{
								noticiasService.grabarNoticiasBloomberg(listadoGrabar);
								listadoGrabar.clear();
								grabados += 1000;
							}

						}
						catch (Exception ex)
						{
							log.error("Error procesando: " + fNoticia.getAbsolutePath());
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
				noticiasService.grabarNoticiasBloomberg(listadoGrabar);
				log.info("Leidos: " + leidos + ". Grabados: " + grabados);
			}

		}
		else

		{
			log.error("No existe la ruta de noticias Bloomberg");
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

	public String getRutaArchivosBloomberg()
	{
		return rutaArchivosBloomberg;
	}

	public void setRutaArchivosBloomberg(String rutaArchivosBloomberg)
	{
		this.rutaArchivosBloomberg = rutaArchivosBloomberg;
	}

}
