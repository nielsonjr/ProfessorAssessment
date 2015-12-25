package com.pa.extractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pa.database.impl.DatabaseFacade;
import com.pa.entity.Curriculo;
import com.pa.entity.Publication;
import com.pa.entity.PublicationType;
import com.pa.exception.InvalidPatternFileException;
import com.pa.util.EnumPublicationLocalType;

public class XMLExtractor {

	public Curriculo lattesExtractor(InputStream file) throws InvalidPatternFileException {
		Curriculo curriculo = null;
		
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			//Load and Parse the XML document
			Document document = builder.parse(file);
			
			curriculo = this.extractBasicInformations(document);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			throw new InvalidPatternFileException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			throw new InvalidPatternFileException(e.getMessage());
		}
		
		if (curriculo == null) {
			throw new InvalidPatternFileException("XML file is invalid: the file is not about a lattes curriculum");
		}

		return curriculo;
	}
	
	private Curriculo extractBasicInformations(Document document) {
		Curriculo curriculo = null;
		String name;
		Long id = null;
		Date lastUpdate = null;

		if (document.getDocumentElement().getNodeName().equals("CURRICULO-VITAE")) {
			// Get last update from xml
			String update = document.getDocumentElement().getAttributes().getNamedItem("DATA-ATUALIZACAO").getNodeValue();

			try {
				SimpleDateFormat sdf1= new SimpleDateFormat("ddMMyyyy");
				lastUpdate = sdf1.parse(update);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// Get identifier
			String identifier = document.getDocumentElement().getAttributes().getNamedItem("NUMERO-IDENTIFICADOR").getNodeValue();
			id = Long.valueOf(identifier);

			//Iterating through the nodes and extracting the data.
			NodeList nodeList = document.getDocumentElement().getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);

				if (node instanceof Element) {
					if (node.getNodeName().equals("DADOS-GERAIS")) {
						// Get name
						name = node.getAttributes().getNamedItem("NOME-COMPLETO").getNodeValue();

						if (id != null && !name.isEmpty() && lastUpdate != null) {
							curriculo = new Curriculo(name, lastUpdate);
							curriculo.setId(id);
						}
					}
					else if(node.getNodeName().equals("PRODUCAO-BIBLIOGRAFICA")) {
						curriculo.setPublications(this.extractPublications(node));
					}
					else if(node.getNodeName().equals("OUTRA-PRODUCAO")) {
						// Orientações concluídas
						int orientations = this.extractQtdOrientations(node, "ORIENTACOES-CONCLUIDAS");
						curriculo.setConcludedOrientations(orientations);
					}
					else if (node.getNodeName().equals("DADOS-COMPLEMENTARES")) {
						// Orientações não concluídas
						int orientations = this.extractQtdOrientations(node, "ORIENTACOES-EM-ANDAMENTO");
						curriculo.setOnGoingOrientations(orientations);
					}
				}
			}
		}

		return curriculo;
	}
	
	private Set<Publication> extractPublications(Node nodeProduction) {
		Set<Publication> publications = new HashSet<Publication>();
		
		NodeList nodeList = nodeProduction.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			
			if (node instanceof Element) {
				if (node.getNodeName().equals("TRABALHOS-EM-EVENTOS")) {
					NodeList events = node.getChildNodes();
					
					for (int j = 0; j < events.getLength(); j++) {
						// Evento (Conferência)
						Node event = events.item(j);
						Node basicDataEvent = event.getChildNodes().item(0);
						
						if (basicDataEvent.getNodeName().equals("DADOS-BASICOS-DO-TRABALHO")) {
							Node eventTitle = basicDataEvent.getAttributes().getNamedItem("TITULO-DO-TRABALHO");
							Node eventYear = basicDataEvent.getAttributes().getNamedItem("ANO-DO-TRABALHO");

							if (eventTitle != null && eventYear != null) {
								PublicationType type = getPublicationType(event, EnumPublicationLocalType.CONFERENCE);
								Publication publication = new Publication(eventTitle.getNodeValue(), Integer.valueOf(eventYear.getNodeValue()), type);

								// Update objects if the publication already exists
								publication = getRealPublication(publication);
								
								publications.add(publication);
							}
						}
					}
				}
				else if(node.getNodeName().equals("ARTIGOS-PUBLICADOS")) {
					NodeList articles = node.getChildNodes();

					for (int j = 0; j < articles.getLength(); j++) {
						// Artigo (Periodico ou Revista)
						Node article = articles.item(j);
						Node basicDataArticle = article.getChildNodes().item(0);

						if (basicDataArticle.getNodeName().equals("DADOS-BASICOS-DO-ARTIGO")) {
							Node articleTitle = basicDataArticle.getAttributes().getNamedItem("TITULO-DO-ARTIGO");
							Node articleYear = basicDataArticle.getAttributes().getNamedItem("ANO-DO-ARTIGO");

							if (articleTitle != null && articleYear != null) {
								PublicationType type = getPublicationType(article, EnumPublicationLocalType.PERIODIC);
								Publication publication = new Publication(articleTitle.getNodeValue(), Integer.valueOf(articleYear.getNodeValue()), type);
								
								// Update objects if the publication already exists
								publication = getRealPublication(publication);

								publications.add(publication);
							}
						}
					}
				}
			}
		}
		
		return publications;
	}
	
	private Publication getRealPublication(Publication publication) {
		List<Publication> databasePublication = DatabaseFacade.getInstance().listAllPublications(publication);
		if (!databasePublication.isEmpty()) {
			for (Publication basePublication : databasePublication) {
				if (basePublication.getPublicationType().equals(publication.getPublicationType())) {
					publication = basePublication;
				}
			}
		}
		
		return publication;
	}
	
	private PublicationType getPublicationType(Node mainNode, EnumPublicationLocalType local) {
		PublicationType type = null;
		String name;
		
		Node details = mainNode.getChildNodes().item(1);
		Node eventName = null;
		
		if (local.equals(EnumPublicationLocalType.CONFERENCE)) {
			if (details.getNodeName().equals("DETALHAMENTO-DO-TRABALHO")) {
				eventName = details.getAttributes().getNamedItem("NOME-DO-EVENTO");
			}
		}
		else if (local.equals(EnumPublicationLocalType.PERIODIC)) {
			if (details.getNodeName().equals("DETALHAMENTO-DO-ARTIGO")) {
				eventName = details.getAttributes().getNamedItem("TITULO-DO-PERIODICO-OU-REVISTA");
			}
		}
		
		if (eventName != null) {
			name = eventName.getNodeValue();
			type = new PublicationType(name, local);
			
			// Update objects if the publication already exists
			type = getRealPublicationType(type);
		}
		
		return type;
	}
	
	private PublicationType getRealPublicationType(PublicationType newType) {
		PublicationType databaseType = DatabaseFacade.getInstance().getPublicationTypeByNameAndType(newType.getName(), newType.getType());
		if (databaseType != null) {
			newType = databaseType;
		}
		
		return newType;
	}
	
	private int extractQtdOrientations(Node mainNode, String tag) {
		int orientations = 0;
		
		NodeList nodeList = mainNode.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			
			if (node instanceof Element) {
				if (node.getNodeName().equals(tag)) {
					NodeList childrenList = node.getChildNodes();
					orientations = childrenList.getLength();
				}
			}
		}
		
		return orientations;
	}
}
