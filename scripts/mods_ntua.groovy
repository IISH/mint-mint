//Set the url attribute for recordInfoNote
contentPrURL = template.findFirst("//mods:recordInfo/mods:recordInfoNote");
contentPrURL.getAttributeByName("@type").addConstantMapping("url");

//Add the Europeana rights
rightsType = template.findFirst("//mods:accessCondition");
rightsType.addEnumeration("http://www.europeana.eu/rights/rr-f/");
rightsType.addEnumeration("http://www.europeana.eu/rights/rr-p/");
rightsType.addEnumeration("http://www.europeana.eu/rights/unknown/");
rightsType.addEnumeration("http://creativecommons.org/publicdomain/mark/1.0/");
rightsType.addEnumeration("http://creativecommons.org/publicdomain/zero/1.0/");
rightsType.addEnumeration("http://creativecommons.org/licenses/by/4.0/");
rightsType.addEnumeration("http://creativecommons.org/licenses/by-sa/4.0/");
rightsType.addEnumeration("http://creativecommons.org/licenses/by-nc/4.0/");
rightsType.addEnumeration("http://creativecommons.org/licenses/by-nc-sa/4.0/");
rightsType.addEnumeration("http://creativecommons.org/licenses/by-nd/4.0/");
rightsType.addEnumeration("http://creativecommons.org/licenses/by-nc-nd/4.0/");
rightsType.addEnumeration("http://www.europeana.eu/rights/out-of-copyright-non-commercial/");
rightsType.addEnumeration("http://www.europeana.eu/rights/orphan-work-eu/");

//Duplicate mods:url and set attributes
isShownAt = template.findFirst("//mods:location/mods:url");
isShownBy = cache.duplicate(template.findFirst("//mods:location/mods:url").getId());
isShownBy.getAttributeByName("@access").addConstantMapping("raw object");
isShownAt.getAttributeByName("@access").addConstantMapping("object in context");

//Control genre by vocabularies
genre = template.findFirst("//mods:subject/mods:genre");
genre.setThesaurus(MappingPrimitives.thesaurus("http://www.socialhistoryportal.org/themes#HopeThemes"));

//Set the bookmarks
mappings.addBookmarkForXpath("Record ID", "/mods/recordInfo/recordIdentifier");
mappings.addBookmarkForXpath("Content provider info", "/mods/recordInfo/recordContentSource");
mappings.addBookmark("Content provider url", contentPrURL);
mappings.addBookmark("EDM Rights", rightsType);
mappings.addBookmarkForXpath("EDM Type", "/mods/typeOfResource");
mappings.addBookmark("Is shown by", isShownBy);
mappings.addBookmark("Is shown at", isShownAt);
mappings.addBookmark("Genre", genre);