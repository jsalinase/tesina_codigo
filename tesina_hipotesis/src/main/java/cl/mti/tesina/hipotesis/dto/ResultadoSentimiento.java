package cl.mti.tesina.hipotesis.dto;

public class ResultadoSentimiento
{
	private int codigoResultado = CodigosResultados.OK;
	protected String sentimiento;
	protected Float positivo;
	protected Float negativo;
	protected Float neutral;
	protected Float mixto;

	public String getSentimiento()
	{
		return sentimiento;
	}

	public void setSentimiento(String sentimiento)
	{
		this.sentimiento = sentimiento;
	}

	public Float getPositivo()
	{
		return positivo;
	}

	public void setPositivo(Float positivo)
	{
		this.positivo = positivo;
	}

	public Float getNegativo()
	{
		return negativo;
	}

	public void setNegativo(Float negativo)
	{
		this.negativo = negativo;
	}

	public Float getNeutral()
	{
		return neutral;
	}

	public void setNeutral(Float neutral)
	{
		this.neutral = neutral;
	}

	public int getCodigoResultado()
	{
		return codigoResultado;
	}

	public void setCodigoResultado(int codigoResultado)
	{
		this.codigoResultado = codigoResultado;
	}

	@Override
	public String toString()
	{
		return "ResultadoSentimiento [codigoResultado=" + codigoResultado + ", sentimiento=" + sentimiento
				+ ", positivo=" + positivo + ", negativo=" + negativo + ", neutral=" + neutral + ", mixto=" + mixto
				+ "]";
	}

	public Float getMixto()
	{
		return mixto;
	}

	public void setMixto(Float mixto)
	{
		this.mixto = mixto;
	}
}
