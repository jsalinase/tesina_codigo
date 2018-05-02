package cl.mti.tesina.analisis.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.DetectDominantLanguageRequest;
import com.amazonaws.services.comprehend.model.DetectDominantLanguageResult;
import com.amazonaws.services.comprehend.model.DetectSentimentRequest;
import com.amazonaws.services.comprehend.model.DetectSentimentResult;
import com.amazonaws.services.comprehend.model.DominantLanguage;

import cl.mti.tesina.analisis.dto.CodigosResultados;
import cl.mti.tesina.analisis.dto.Idiomas;
import cl.mti.tesina.analisis.dto.ResultadoSentimiento;
import cl.mti.tesina.analisis.dto.Sentimiento;
import cl.mti.tesina.analisis.services.AnalizadorSentimientosService;

//@Component("amazonComprehendSentimientosService")
public class AmazonComprehendSentimientosServiceImpl implements AnalizadorSentimientosService
{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private AWSCredentialsProvider awsCreds;
	private AmazonComprehend clienteComprehend;

	/**
	 * Constructor de Amazon Comprehend Engine
	 */
	public AmazonComprehendSentimientosServiceImpl()
	{
		awsCreds = DefaultAWSCredentialsProviderChain.getInstance();
		clienteComprehend = AmazonComprehendClientBuilder.standard().withCredentials(awsCreds).build();
	}

	@Override
	public ResultadoSentimiento procesar(String texto, String idioma)
	{
		log.debug("Llamando a API de Sentimientos");

		if (texto.length() > 5000)
		{
			texto = texto.substring(0, 4950);
		}

		log.debug("Largo Request: " + texto.length());

		ResultadoSentimiento resultado = new ResultadoSentimiento();

		try
		{
			DetectSentimentRequest request = new DetectSentimentRequest().withText(texto).withLanguageCode(idioma);

			DetectSentimentResult response = clienteComprehend.detectSentiment(request);
			log.debug("Respuesta: " + response);

			if (response != null)
			{
				resultado.setPositivo(response.getSentimentScore().getPositive());
				resultado.setNegativo(response.getSentimentScore().getNegative());
				resultado.setNeutral(response.getSentimentScore().getNeutral());
				resultado.setMixto(response.getSentimentScore().getMixed());

				if (response.getSentiment().equals("NEUTRAL"))
				{
					resultado.setSentimiento(Sentimiento.NEUTRAL);
				}
				else if (response.getSentiment().equals("NEGATIVE"))
				{
					resultado.setSentimiento(Sentimiento.NEGATIVO);
				}
				else if (response.getSentiment().equals("POSITIVE"))
				{
					resultado.setSentimiento(Sentimiento.POSITIVO);
				}
				else if (response.getSentiment().equals("MIXED"))
				{
					resultado.setSentimiento(Sentimiento.MIXTO);
				}

			}
			else
			{
				resultado.setCodigoResultado(CodigosResultados.NO_OK);
			}
		}
		catch (Exception ex)
		{
			resultado.setCodigoResultado(CodigosResultados.NO_OK);
			log.error("Error calculando el sentimiento con Amazon Comprehend", ex);
		}

		return resultado;
	}

	@Override
	public ResultadoSentimiento procesar(String texto)
	{
		DetectDominantLanguageRequest request = new DetectDominantLanguageRequest().withText(texto);
		DetectDominantLanguageResult response = clienteComprehend.detectDominantLanguage(request);
		String lenguaje = Idiomas.INGLES;
		if (response != null)
		{
			Float score = 0f;
			for (DominantLanguage l : response.getLanguages())
			{
				if (l.getScore().floatValue() > score)
				{
					score = l.getScore();
					lenguaje = l.getLanguageCode();
				}
			}
		}
		return procesar(texto, lenguaje);
	}

	@Override
	public int getIdMotor()
	{
		return 0;
	}

}
