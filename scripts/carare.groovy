carareId = template.getAttributes().get(0);
carareId.addConstantMapping("carare:000000");
carareId.setFixed(true);


////////////////////
// Bookmarks
////////////////////
mappings.addBookmarkForXpath("Identifier", "/carare/heritageAssetIdentification/recordInformation/id");
mappings.addBookmarkForXpath("Title", "/carare/heritageAssetIdentification/appellation/name");
//mappings.addBookmarkForXpath("Start Date", "/carare/heritageAssetIdentification/characters/temporal/timeSpan/startDate");
//mappings.addBookmarkForXpath("End Data", "/carare/heritageAssetIdentification/characters/temporal/timeSpan/endDate");
mappings.addBookmarkForXpath("Display Date", "/carare/heritageAssetIdentification/characters/temporal/displayDate");
mappings.addBookmarkForXpath("Description", "/carare/heritageAssetIdentification/description");
mappings.addBookmarkForXpath("EDM type (format,type)", "/carare/heritageAssetIdentification/generalType");
mappings.addBookmarkForXpath("DC Identifier", "/carare/heritageAssetIdentification/appellation/id");
mappings.addBookmarkForXpath("Language", "/carare/heritageAssetIdentification/recordInformation/language");
mappings.addBookmarkForXpath("Publisher", "/carare/heritageAssetIdentification/publicationStatement/publisher");
//mappings.addBookmarkForXpath("Has Part", "/carare/heritageAssetIdentification/hasPart");
//mappings.addBookmarkForXpath("Is Part Of", "/carare/heritageAssetIdentification/isPartOf");
mappings.addBookmarkForXpath("DC Rights", "/carare/heritageAssetIdentification/rights/copyrightCreditLine");
mappings.addBookmarkForXpath("Source", "/carare/heritageAssetIdentification/recordInformation/source");
mappings.addBookmarkForXpath("Subject", "/carare/heritageAssetIdentification/characters/heritageAssetType");
//mappings.addBookmarkForXpath("Type", "/carare/heritageAssetIdentification/generalType");
mappings.addBookmarkForXpath("Dimensions (Extent)", "/carare/heritageAssetIdentification/characters/dimensions");
mappings.addBookmarkForXpath("Material", "/carare/heritageAssetIdentification/characters/materials");
mappings.addBookmarkForXpath("Provenance", "/carare/heritageAssetIdentification/provenance");

mappings.addBookmarkForXpath("Image URI", "/carare/digitalResource/recordInformation/id");
mappings.addBookmarkForXpath("Image URI (appellation id)", "/carare/digitalResource/appellation/id");
mappings.addBookmarkForXpath("Link name", "/carare/digitalResource/appellation/name");
mappings.addBookmarkForXpath("Image URI (edm:object)", "/carare/digitalResource/object");
mappings.addBookmarkForXpath("Metadata URI (edm:isShownAt)", "/carare/digitalResource/isShownAt");



mappings.addBookmarkForXpath("Europeana rights", "/carare/heritageAssetIdentification/rights/europeanaRights");

