package cl.mti.tesina.rawdata.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.mti.tesina.rawdata.entities.Noticias;
import cl.mti.tesina.rawdata.repositories.NoticiasRepository;

@Service
public class NoticiasService
{

	public static final String BLOOMBERG = "Bloomberg";
	public static final String REUTERS = "Reuters";
	public static final String TWITTER = "Twitter";

	@Autowired
	private NoticiasRepository noticiasRepository;

	public void grabarNoticias(List<Noticias> listado)
	{
		noticiasRepository.saveAll(listado);
	}

	public void grabarNoticiasBloomberg(List<Noticias> listado)
	{
		for (Noticias n : listado)
		{
			n.setFuente(BLOOMBERG);
		}
		noticiasRepository.saveAll(listado);
	}

	public void grabarNoticiasReuters(List<Noticias> listado)
	{
		for (Noticias n : listado)
		{
			n.setFuente(REUTERS);
		}
		noticiasRepository.saveAll(listado);
	}

	public void grabarNoticiasTwitter(List<Noticias> listado)
	{
		for (Noticias n : listado)
		{
			n.setFuente(TWITTER);
		}
		noticiasRepository.saveAll(listado);
	}

	public void grabarNoticia(Noticias n)
	{
		noticiasRepository.save(n);
	}

	public void grabarNoticiaBloomberg(Noticias n)
	{
		n.setFuente(BLOOMBERG);
		grabarNoticia(n);
	}

	public void grabarNoticiaReuters(Noticias n)
	{
		n.setFuente(REUTERS);
		grabarNoticia(n);
	}

	public void grabarNoticiaTwitter(Noticias n)
	{
		n.setFuente(TWITTER);
		grabarNoticia(n);
	}

	public Optional<Noticias> obtenerNoticia(Integer pk)
	{
		return noticiasRepository.findById(pk);
	}

}
