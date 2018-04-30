package cl.mti.tesina.analisis.services.impl;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cl.mti.tesina.analisis.dto.CodigosResultados;
import cl.mti.tesina.analisis.dto.ResultadoSentimiento;
import cl.mti.tesina.analisis.dto.Sentimiento;
import cl.mti.tesina.analisis.services.AnalizadorSentimientosService;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

@Component("stanfordNlpSentimientosService")
public class StanfordNLPSentimientosServiceImpl implements AnalizadorSentimientosService
{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private Properties props = new Properties();
	private StanfordCoreNLP pipeline = null;

	/**
	 * Constructor de Amazon Comprehend Engine
	 */
	public StanfordNLPSentimientosServiceImpl()
	{
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
	}

	@Override
	public ResultadoSentimiento procesar(String texto, String idioma)
	{
		log.debug("Llamando a API de Sentimientos de Stanford");
		int mainSentiment = 2;
		if (texto != null && texto.length() > 0)
		{
			int longest = 0;
			Annotation annotation = pipeline.process(texto);
			for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class))
			{
				Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				String partText = sentence.toString();
				if (partText.length() > longest)
				{
					mainSentiment = sentiment;
					longest = partText.length();
				}

			}
		}

		ResultadoSentimiento resultado = new ResultadoSentimiento();
		if (mainSentiment >= 0)
		{
			if ((mainSentiment == 0) || (mainSentiment == 1))
			{
				resultado.setSentimiento(Sentimiento.NEGATIVO);
				resultado.setNegativo((float)mainSentiment);
			}
			else if ((mainSentiment == 3) || (mainSentiment == 4))
			{
				resultado.setSentimiento(Sentimiento.POSITIVO);
				resultado.setPositivo((float)mainSentiment);
			}
			else if (mainSentiment == 0)
			{
				resultado.setSentimiento(Sentimiento.NEUTRAL);
				resultado.setNeutral((float)mainSentiment);
			}
			else
			{
				resultado.setCodigoResultado(CodigosResultados.NO_OK);
			}
		}
		else
		{
			resultado.setCodigoResultado(CodigosResultados.NO_OK);
		}

		return resultado;
	}

	@Override
	public ResultadoSentimiento procesar(String texto)
	{
		return procesar(texto, "en");
	}

}
