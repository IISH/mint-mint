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


//Get Europeana rights under ore:Aggregation
edmRights = template.find("//edm:rights/@rdf:resource").get(1);

//Constant mapping for edm:provider
edmProvider = template.findFirst("//edm:provider");
edmProvider.addConstantMapping("Europeana Sounds");
edmProvider.setFixed(true);

//Create europeana classification
itemGenre = cache.duplicate(template.findFirst("//edm:itemGenre").getId());
itemGenre.setLabel("itemGenre (vocabulary)");
itemGenreRes = itemGenre.getAttribute("@rdf:resource");

hasGenre = cache.duplicate(template.findFirst("//ebucore:hasGenre").getId());
hasGenre.setLabel("hasGenre (vocabulary)");
hasGenreRes = hasGenre.getAttribute("@rdf:resource");

medium = cache.duplicate(template.findFirst("//dcterms:medium").getId());
medium.setLabel("medium (vocabulary)");
mediumRes = medium.getAttribute("@rdf:resource");



itemGenreRes.setThesaurus(MappingPrimitives.thesaurus("http://data.europeana.eu/concept/soundgenres"));
hasGenreRes.setThesaurus(MappingPrimitives.thesaurus("http://data.europeana.eu/concept/soundgenres"));
mediumRes.setThesaurus(MappingPrimitives.thesaurus("http://rdvocab.info/termList/RDACarrierType"));

/**************** THESE ARE COMMENTS
// Vocabs
handlers = template.find("//edm:itemGenre/@rdf:resource");
for(Element conceptID: handlers) {
    conceptID.setThesaurus(MappingPrimitives.thesaurus("http://data.europeana.eu/concept/music/genre/Music_Genre_Scheme"));
}

handlers = template.find("//ebucore:hasGenre/@rdf:resource");
for(Element conceptID: handlers) {
    conceptID.setThesaurus(MappingPrimitives.thesaurus("http://data.europeana.eu/concept/music/genre/Music_Genre_Scheme"));
}

handlers = template.find("//dcterms:medium/@rdf:resource");
for(Element conceptID: handlers) {
    conceptID.setThesaurus(MappingPrimitives.thesaurus("http://rdvocab.info/termList/RDACarrierType"));
}

handlers = template.find("//dc:type/@rdf:resource");
for(Element conceptID: handlers) {
    conceptID.setThesaurus(MappingPrimitives.thesaurus("http://data.europeana.eu/concept/sound/genre/Generic_Sound_Scheme"));
}

handlers = template.find("//edm:hasType/@rdf:resource");
for(Element conceptID: handlers) {
    conceptID.setThesaurus(MappingPrimitives.thesaurus("http://data.europeana.eu/concept/sound/genre/Soundscapes_Scheme"));
}
*********************************************/

handlers = template.find("//@xml:lang");
for(Element handler: handlers) {
  handler.setThesaurus(MappingPrimitives.vocabulary("http://mint.image.ece.ntua.gr/Vocabularies/Languages/LangThesaurus"));
}




////////////////////
// Bookmarks
////////////////////
mappings.addBookmarkForXpath("Title", "/RDF/ProvidedCHO/title");
mappings.addBookmarkForXpath("Description", "/RDF/ProvidedCHO/description");
mappings.addBookmarkForXpath("Generic Sound Scheme", "/RDF/ProvidedCHO/type");
mappings.addBookmark("Music Genre", template.find("//ebucore:hasGenre").get(1));
mappings.addBookmarkForXpath("Music Genre (Vocabulary)", "/RDF/ProvidedCHO/hasGenre");
mappings.addBookmark("RDA Carrier Type", template.find("//dcterms:medium").get(1));
mappings.addBookmarkForXpath("RDA Carrier Type (Vocabulary)", "/RDF/ProvidedCHO/medium/@resource");
mappings.addBookmarkForXpath("Soundscapes Scheme", "/RDF/ProvidedCHO/hasType");



mappings.addBookmarkForXpath("Data Provider", "/RDF/Aggregation/dataProvider");
mappings.addBookmark("EDM Rights", edmRights);
mappings.addBookmarkForXpath("Link to DCHO", "/RDF/Aggregation/isShownBy/@resource");
mappings.addBookmarkForXpath("Link to Metadata (site)", "/RDF/Aggregation/isShownAt/@resource");
mappings.addBookmark("Collection Genre", template.find("//edm:itemGenre").get(1));
mappings.addBookmarkForXpath("Collection Genre (Vocabulary)", "/RDF/Collection/itemGenre");






