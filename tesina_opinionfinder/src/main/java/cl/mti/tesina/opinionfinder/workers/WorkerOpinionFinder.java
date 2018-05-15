package cl.mti.tesina.opinionfinder.workers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import cl.mti.tesina.opinionfinder.Estadisticas;
import cl.mti.tesina.opinionfinder.entities.Noticias;
import cl.mti.tesina.opinionfinder.entities.Sentimientos;
import cl.mti.tesina.opinionfinder.entities.Stocks;
import cl.mti.tesina.opinionfinder.repositories.NoticiasRepository;
import cl.mti.tesina.opinionfinder.repositories.SentimientosRepository;
import cl.mti.tesina.opinionfinder.repositories.StocksRepository;
import opin.config.Config;
import opin.entity.Corpus;
import opin.featurefinder.ClueFind;
import opin.logic.AnnotationHandler;
import opin.output.SGMLOutput;
import opin.preprocessor.PreProcess;
import opin.rulebased.RuleBasedClassifier;
import opin.supervised.ExpressionPolarityClassifier;
import opin.supervised.SentenceSubjectivityClassifier;

@Component("workerOpinionFinder")
@Scope("prototype")
public class WorkerOpinionFinder implements Runnable
{
	private static final Logger log = LoggerFactory.getLogger(WorkerOpinionFinder.class);

	private int numeroPagina;

	@Autowired
	private NoticiasRepository noticiasRepository;
	@Autowired
	private StocksRepository stocksRepository;
	@Autowired
	private SentimientosRepository sentimientosRepository;
	private static final int TAMANO_PAGINA = 100;
	@Autowired
	private Estadisticas estadisticas;

	private File f;

	private int idMotor = 7;

	@Override
	public void run()
	{
		log.info("Procesando Pagina: " + numeroPagina);

		PageRequest request = PageRequest.of(numeroPagina, TAMANO_PAGINA);
		Page<Stocks> paginaNoticias = stocksRepository.findAll(request);
		List<Sentimientos> listadoSentimientos = new ArrayList<Sentimientos>();

		for (Stocks a : paginaNoticias)
		{
			estadisticas.incrementLeidos();

			Optional<Noticias> n = noticiasRepository.findById(a.getStocksId().getId());
			if (n.isPresent())
			{
				Noticias nn = n.get();

				File archivoActual = null;
				try
				{
					archivoActual = new File(f, nn.getId() + ".txt");
					archivoActual.createNewFile();
					Files.write(archivoActual.toPath(), nn.getTextoProcesado().getBytes());
				}
				catch (Exception ex)
				{
					estadisticas.incrementErrores();
					continue;
				}

				List<String> argumentos = new ArrayList<String>();
				argumentos.add(archivoActual.getAbsolutePath());

				PrintStream originalStream = System.out;
				PrintStream originalStreamErr = System.err;

				PrintStream dummyStream = new PrintStream(new OutputStream()
				{
					public void write(int b)
					{
						// NO-OP
					}
				});

				System.setOut(dummyStream);
				System.setErr(dummyStream);

				Config conf = new Config();
				if (!conf.parseCommandLineOptions(argumentos.stream().toArray(String[]::new)))
				{
					return;
				}

				Corpus corpus = new Corpus(conf);

				if (conf.isRunPreprocessor())
				{
					PreProcess preprocessor = new PreProcess(conf);
					preprocessor.process(corpus);
				}

				if (conf.isRunClueFinder())
				{
					ClueFind clueFinder = new ClueFind(conf);
					clueFinder.process(corpus);
				}

				AnnotationHandler annHandler = new AnnotationHandler(conf);
				if (conf.isRunRulebasedClassifier() || conf.isRunSubjClassifier() || conf.isRunPolarityClassifier())
				{
					annHandler.buildSentencesFromGateDefault(corpus);
				}

				if (conf.isRunRulebasedClassifier())
				{
					annHandler.readInRequiredAnnotationsForRuleBased(corpus);
					RuleBasedClassifier rulebased = new RuleBasedClassifier();
					rulebased.process(corpus);
				}

				if (conf.isRunSubjClassifier())
				{
					annHandler.readInRequiredAnnotationsForSubjClassifier(corpus);
					SentenceSubjectivityClassifier subjClassifier = new SentenceSubjectivityClassifier(conf);
					subjClassifier.process(corpus);
				}

				if (conf.isRunPolarityClassifier())
				{
					annHandler.readInRequiredAnnotationsForPolarityClassifier(corpus);
					ExpressionPolarityClassifier polarityClassifier = new ExpressionPolarityClassifier(conf);
					polarityClassifier.process(corpus);
				}

				if (conf.isRunSGMLOutput())
				{
					SGMLOutput output = new SGMLOutput(conf.isRunRulebasedClassifier(), conf.isRunSubjClassifier(),
							conf.isRunPolarityClassifier());
					output.process(corpus);
				}

				File archivoResultado = new File(archivoActual + "_auto_anns" + File.separator + "exp_polarity.txt");

				int total = 0;
				int positivo = 0;
				int negativo = 0;
				int neutral = 0;

				try
				{
					BufferedReader br = new BufferedReader(new FileReader(archivoResultado));
					String linea = null;

					while ((linea = br.readLine()) != null)
					{
						total++;
						String[] splitPortab = linea.split("\\t");
						if (splitPortab[1].equals("positive"))
						{
							positivo++;
						}
						else if (splitPortab[1].equals("negative"))
						{
							negativo++;
						}
						else if (splitPortab[1].equals("neutral"))
						{
							neutral++;
						}
					}

					System.setOut(originalStream);
					System.setErr(originalStreamErr);

					Sentimientos sentimientos = new Sentimientos();

					sentimientos.setIdMotor(idMotor);
					sentimientos.setNoticia(nn);
					sentimientos.setPuntajeMixto((float) total);
					sentimientos.setPuntajeNegativo((float) negativo);
					sentimientos.setPuntajePositivo((float) positivo);
					sentimientos.setPuntajeNeutral((float) neutral);
					if ((neutral > positivo) && (neutral > negativo))
					{
						sentimientos.setSentimiento("NEUTRAL");
					}
					else if ((positivo >= neutral) && (positivo > negativo))
					{
						sentimientos.setSentimiento("POSITIVO");
					}
					else if ((negativo >= neutral) && (negativo > positivo))
					{
						sentimientos.setSentimiento("POSITIVO");
					}
					else
					{
						sentimientos.setSentimiento("NEUTRAL");
					}

					listadoSentimientos.add(sentimientos);

					if (listadoSentimientos.size() > 100)
					{
						sentimientosRepository.saveAll(listadoSentimientos);
						listadoSentimientos.clear();
						for (int i = 0; i < 50; i++)
						{
							estadisticas.incrementGrabados();
						}
					}

					br.close();
				}
				catch (Exception ex)
				{
					estadisticas.incrementErrores();
					continue;
				}
			}
		}
		if (listadoSentimientos.size() > 0)
		{
			sentimientosRepository.saveAll(listadoSentimientos);
		}

		log.info("Terminando Pagina: " + numeroPagina);
		log.info(estadisticas.toString());

	}

	public int getNumeroPagina()
	{
		return numeroPagina;
	}

	public void setNumeroPagina(int numeroPagina)
	{
		this.numeroPagina = numeroPagina;
	}

	public File getF()
	{
		return f;
	}

	public void setF(File f)
	{
		this.f = f;
	}

}
