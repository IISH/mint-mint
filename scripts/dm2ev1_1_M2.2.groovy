handlers = template.find("//edm:ProvidedCHO/rdf:type/@rdf:resource");
for(Element providedCHO: handlers) {
    if(providedCHO != null){
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Document", "Document");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/File", "File");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Fragment", "Fragment");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Manuscript", "Manuscript");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Page", "Page");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Photo", "Photo");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Work", "Work");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Paragraph", "Paragraph");
		providedCHO.addEnumeration("http://purl.org/ontology/bibo/Journal", "Journal");
		providedCHO.addEnumeration("http://purl.org/ontology/bibo/Letter", "Letter");
		providedCHO.addEnumeration("http://purl.org/ontology/bibo/Issue", "Issue");
		providedCHO.addEnumeration("http://purl.org/ontology/bibo/Book", "Book");
		providedCHO.addEnumeration("http://purl.org/ontology/bibo/Manuscript", "Manuscript");
		providedCHO.addEnumeration("http://purl.org/ontology/bibo/Series", "Series");
    	providedCHO.addEnumeration("http://purl.org/spar/fabio/Cover", "Cover");
    	providedCHO.addEnumeration("http://purl.org/spar/fabio/Article", "Article");
    	providedCHO.addEnumeration("http://purl.org/spar/fabio/Chapter", "Chapter");
	}
}

handlers = template.find("//edm:ProvidedCHO/dc:type/@rdf:resource");
for(Element providedCHO: handlers) {
    if(Element !=null ){
    	providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Document", "Document");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/File", "File");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Fragment", "Fragment");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Manuscript", "Manuscript");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Page", "Page");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Photo", "Photo");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Work", "Work");
		providedCHO.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Paragraph", "Paragraph");
		providedCHO.addEnumeration("http://purl.org/ontology/bibo/Journal", "Journal");
		providedCHO.addEnumeration("http://purl.org/ontology/bibo/Letter", "Letter");
		providedCHO.addEnumeration("http://purl.org/ontology/bibo/Issue", "Issue");
		providedCHO.addEnumeration("http://purl.org/ontology/bibo/Book", "Book");
		providedCHO.addEnumeration("http://purl.org/ontology/bibo/Manuscript", "Manuscript");
		providedCHO.addEnumeration("http://purl.org/ontology/bibo/Series", "Series");
    	providedCHO.addEnumeration("http://purl.org/spar/fabio/Cover", "Cover");
    	providedCHO.addEnumeration("http://purl.org/spar/fabio/Article", "Article");
    	providedCHO.addEnumeration("http://purl.org/spar/fabio/Chapter", "Chapter");
	}
}

// assign types to Organisation rdf:type
handlers = template.find("//foaf:Organisation/rdf:type/@rdf:resource");
for(Element org: handlers) {
	org.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Archive", "Archive");
	org.addEnumeration("http://vivoweb.org/ontology/core#Library", "Library");
	org.addEnumeration("http://vivoweb.org/ontology/core#Museum", "Museum");
	org.addEnumeration("http://vivoweb.org/ontology/core#University", "University");
}

handlers = template.find("//edm:Agent")
for(Element agent: handlers) {
    rdfType = agent.findFirst("rdf:type/@rdf:resource");
    if(rdfType !=null ){
    	rdfType.addEnumeration("http://xmlns.com/foaf/0.1/Person", "Person");
    	rdfType.addEnumeration("http://xmlns.com/foaf/0.1/Organisation", "Organisation");
		rdfType.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Archive", "Archive");
		rdfType.addEnumeration("http://vivoweb.org/ontology/core#Library", "Library");
		rdfType.addEnumeration("http://vivoweb.org/ontology/core#Museum", "Museum");
		rdfType.addEnumeration("http://vivoweb.org/ontology/core#University", "University");
	}
	
	dateOfEstablishment = agent.findFirst("rdaGr2:dateOfEstablishment");
	if(dateOfEstablishment !=null ){
	    //Organisation
	    dateOfEstablishment.setLabel("dateOfEstablishment (applies to foaf:Organisation only)")
	}
	
	dateOfTermination = agent.findFirst("rdaGr2:dateOfTermination");
	if(dateOfTermination !=null ){
		//Organisation
	   dateOfTermination.setLabel("dateOfTermination (applies to foaf:Organisation only)")
	}
	
	gender = agent.findFirst("rdaGr2:gender");
	if(gender !=null ){
	  	//Person
		gender.setLabel("gender (applies to foaf:Person only)")
	}
	professionOrOccupation = agent.findFirst("rdaGr2:professionOrOccupation");
	if(professionOrOccupation !=null ){
		//Person
		professionOrOccupation.setLabel("professionOrOccupation (applies to foaf:Person only)")
	}
	

	biographicalInformation = agent.findFirst("rdaGr2:biographicalInformation");
	if(biographicalInformation !=null ){
		//Person
		biographicalInformation.setLabel("biographicalInformation (applies to foaf:Person only)")
	}
	
	dateOfBirth = agent.findFirst("rdaGr2:dateOfBirth");
	if(dateOfBirth !=null ){
		//Person
		dateOfBirth.setLabel("dateOfBirth (applies to foaf:Person only)")
	}
	
	dateOfDeath = agent.findFirst("rdaGr2:dateOfDeath");
	if(dateOfDeath !=null ){
		//Person
		dateOfDeath.setLabel("dateOfDeath (applies to foaf:Person only)")
	}
	
	influencedBy = agent.findFirst("dm2e:influencedBy");
	if(influencedBy !=null ){
		//Person
		influencedBy.setLabel("influencedBy (applies to foaf:Person only)")
	}
	
	studentOf = agent.findFirst("dm2e:studentOf");
	if(studentOf !=null ){
		//Person
		studentOf.setLabel("studentOf (applies to foaf:Person only)")
	}
}

// assign types to Concept rdf:type
handlers = template.find("//skos:Concept/rdf:type/@rdf:resource")
for(Element skosConcept: handlers) {
	skosConcept.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Document", "Document");
		skosConcept.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/File", "File");
		skosConcept.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Fragment", "Fragment");
		skosConcept.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Manuscript", "Manuscript");
		skosConcept.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Page", "Page");
		skosConcept.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Photo", "Photo");
		skosConcept.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Work", "Work");
		skosConcept.addEnumeration("http://onto.dm2e.eu/schemas/dm2e/Paragraph", "Paragraph");
		skosConcept.addEnumeration("http://purl.org/ontology/bibo/Journal", "Journal");
		skosConcept.addEnumeration("http://purl.org/ontology/bibo/Letter", "Letter");
		skosConcept.addEnumeration("http://purl.org/ontology/bibo/Issue", "Issue");
		skosConcept.addEnumeration("http://purl.org/ontology/bibo/Book", "Book");
		skosConcept.addEnumeration("http://purl.org/ontology/bibo/Manuscript", "Manuscript");
		skosConcept.addEnumeration("http://purl.org/ontology/bibo/Series", "Series");
    	skosConcept.addEnumeration("http://purl.org/spar/fabio/Cover", "Cover");
    	skosConcept.addEnumeration("http://purl.org/spar/fabio/Article", "Article");
    	skosConcept.addEnumeration("http://purl.org/spar/fabio/Chapter", "Chapter");
}

//Enumerated xml langs
handlers = template.find("//@xml:lang");
for(Element handler: handlers) {
    handler.setThesaurus(MappingPrimitives.vocabulary("http://mint.image.ece.ntua.gr/Vocabularies/Languages/LangThesaurus"));
}