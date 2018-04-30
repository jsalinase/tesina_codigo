package cl.mti.tesina.rawdata.workers;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import cl.mti.tesina.rawdata.entities.Noticias;
import cl.mti.tesina.rawdata.services.NoticiasService;

@Component
@ConfigurationProperties(prefix = "tesina")
public class WorkerFilesTwitter implements CommandLineRunner
{
	private static final Logger log = LoggerFactory.getLogger(WorkerFilesTwitter.class);

	private String rutaArchivosTwitter;

	@Autowired
	private NoticiasService noticiasService;

	@Override
	public void run(String... args) throws Exception
	{
		log.info("Procesando Noticias Twitter");
		File directorioArchivosTwitter = new File(rutaArchivosTwitter);

		if (directorioArchivosTwitter.exists())
		{
			int leidos = 0;
			int grabados = 0;

			for (File f : directorioArchivosTwitter.listFiles())
			{
				try
				{
					if (f.length() == 0)
					{
						continue;
					}
					if (!f.getName().endsWith("csv"))
					{
						continue;
					}

					leidos++;

					if (leidos % 10000 == 0)
					{
						log.info("Leidos: " + leidos + ". Grabados: " + grabados);
					}

					Reader reader = Files.newBufferedReader(Paths.get(f.getAbsolutePath()));
					CSVParser parser = new CSVParserBuilder().withSeparator(';').withStrictQuotes(false).withQuoteChar('"').build();

					CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).withSkipLines(1).build();

					String[] nombreArchivo = f.getName().replaceAll("\\.csv", "").split("_");

					SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

					String elRecurso = "";
					if ((nombreArchivo != null) && (nombreArchivo.length == 2))
					{
						elRecurso = nombreArchivo[0];
					}
					else if (nombreArchivo.length > 2)
					{
						for (int i = 0; i < nombreArchivo.length - 1; i++)
						{
							elRecurso += nombreArchivo[i] + "_";
						}
						elRecurso = elRecurso.substring(0, elRecurso.length() - 1);
					}

					String[] nextRecord;
					while ((nextRecord = csvReader.readNext()) != null)
					{
						Noticias n = null;
						try
						{
							n = new Noticias();
							n.setTitulo("");
							n.setAutor(limpiarString(elRecurso));
							n.setTexto(limpiarString(nextRecord[4]));
							n.setFecha(spf.parse(nextRecord[1]));
							n.setRecurso(limpiarString(nextRecord[9]));
							noticiasService.grabarNoticiaTwitter(n);
							grabados++;
						}
						catch (Exception ex)
						{
							log.error("Error en Archivo: " + f.getAbsolutePath());
						}
					}

					reader.close();
				}
				catch (Exception ex)
				{
					log.error("Error procesando: " + f.getName(), ex);
				}
			}
			
		}
	}

	public String limpiarString(String s)
	{
		if (s != null)
		{
			return s.replaceAll("--", "").trim();
			//.replaceAll("[^\\p{ASCII}]", "?")
		}
		else
		{
			return "";
		}
	}

	public String getRutaArchivosTwitter()
	{
		return rutaArchivosTwitter;
	}

	public void setRutaArchivosTwitter(String rutaArchivosTwitter)
	{
		this.rutaArchivosTwitter = rutaArchivosTwitter;
	}

}
