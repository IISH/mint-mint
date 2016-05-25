//Latest Europeana rights
rights = template.find("//edm:rights");
for(Element edmRights : rights) {
	rightsType = edmRights.getAttribute("@rdf:resource")
	rightsType.addEnumeration("http://www.europeana.eu/rights/rr-f/");
	rightsType.addEnumeration("http://www.europeana.eu/rights/rr-p/");
	rightsType.addEnumeration("http://www.europeana.eu/rights/unknown/");
	rightsType.addEnumeration("http://creativecommons.org/publicdomain/mark/1.0/");
	rightsType.addEnumeration("http://creativecommons.org/publicdomain/zero/1.0/");
	rightsType.addEnumeration("http://creativecommons.org/licenses/by/3.0/");
	rightsType.addEnumeration("http://creativecommons.org/licenses/by-sa/3.0/");
	rightsType.addEnumeration("http://creativecommons.org/licenses/by-nc/3.0/");
	rightsType.addEnumeration("http://creativecommons.org/licenses/by-nc-sa/3.0/");
	rightsType.addEnumeration("http://creativecommons.org/licenses/by-nd/3.0/");
	rightsType.addEnumeration("http://creativecommons.org/licenses/by-nc-nd/3.0/");
	rightsType.addEnumeration("http://creativecommons.org/licenses/by/4.0/");
	rightsType.addEnumeration("http://creativecommons.org/licenses/by-sa/4.0/");
	rightsType.addEnumeration("http://creativecommons.org/licenses/by-nc/4.0/");
	rightsType.addEnumeration("http://creativecommons.org/licenses/by-nc-sa/4.0/");
	rightsType.addEnumeration("http://creativecommons.org/licenses/by-nd/4.0/");
	rightsType.addEnumeration("http://creativecommons.org/licenses/by-nc-nd/4.0/");
	rightsType.addEnumeration("http://www.europeana.eu/rights/out-of-copyright-non-commercial/");
	rightsType.addEnumeration("http://www.europeana.eu/rights/orphan-work-eu/");
}

//Constant mapping for edm:provider
edmProvider = template.findFirst("//edm:provider");
edmProvider.addConstantMapping("IALHI");
edmProvider.setFixed(false);

//Control subject by vocabularies
subject = cache.duplicate(template.findFirst("//edm:ProvidedCHO/dc:subject").getId());
subject.setLabel("subject (vocabulary)");
subjectRes = subject.getAttribute("@rdf:resource");
subjectRes.setThesaurus(MappingPrimitives.thesaurus("http://www.socialhistoryportal.org/themes#HopeThemes"));


mappings.addBookmarkForXpath("Title", "/RDF/ProvidedCHO/title");
mappings.addBookmarkForXpath("Description", "/RDF/ProvidedCHO/description");
mappings.addBookmarkForXpath("Language", "/RDF/ProvidedCHO/language");
mappings.addBookmarkForXpath("Identifier", "/RDF/ProvidedCHO/identifier");
mappings.addBookmarkForXpath("Type", "/RDF/ProvidedCHO/type");
mappings.addBookmark("Subject", template.find("//edm:ProvidedCHO/dc:subject").get(1));
mappings.addBookmarkForXpath("Subject (Vocabulary)", "/RDF/ProvidedCHO/subject");
mappings.addBookmarkForXpath("Coverage", "/RDF/ProvidedCHO/coverage");
mappings.addBookmarkForXpath("Spatial", "/RDF/ProvidedCHO/spatial");
mappings.addBookmarkForXpath("Creator", "/RDF/ProvidedCHO/creator");
mappings.addBookmarkForXpath("Date", "/RDF/ProvidedCHO/date");
mappings.addBookmark("EDM Type", template.findFirst("//edm:type"));


mappings.addBookmarkForXpath("Data Provider", "/RDF/Aggregation/dataProvider");
mappings.addBookmark("EDM Rights", template.findFirst("//ore:Aggregation/edm:rights/@rdf:resource"));
mappings.addBookmarkForXpath("Link to DCHO", "/RDF/Aggregation/isShownBy/@resource");
mappings.addBookmarkForXpath("Link to DCHO (thumbnail)", "/RDF/Aggregation/object/@resource");
mappings.addBookmarkForXpath("Link to Metadata (site)", "/RDF/Aggregation/isShownAt/@resource");

handlers = template.find("//@xml:lang");
for(Element handler: handlers) {
  handler.setThesaurus(MappingPrimitives.vocabulary("http://mint.image.ece.ntua.gr/Vocabularies/Languages/LangThesaurus"));
}
