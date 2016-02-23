// Set project title to lidoRecID
schemaId = template.getChild("lido:lidoRecID");
schemaId.addConstantMapping("/" + Config.get("mint.title") + ":000000");
schemaId.setFixed(true);
schemaIdType = schemaId.getAttribute("@lido:type");
schemaIdType.addConstantMapping(Config.get("mint.title"));
schemaIdType.setFixed(true);

// Make the lang title mandatory
template.getChild("lido:descriptiveMetadata").getAttribute("@xml:lang").setMandatory(true);
template.getChild("lido:administrativeMetadata").getAttribute("@xml:lang").setMandatory(true);

//Object Work Type
objectWorkType = template.findFirst("lido:descriptiveMetadata/lido:objectClassificationWrap/lido:objectWorkTypeWrap/lido:objectWorkType");
objectWorkType.setLabel("objectWorkType - Recommended use of AAT")
objectWorkTypeForSkos = objectWorkType.findFirst("lido:conceptID");

// create custom classification schemes
culturalHeritageType = cache.duplicate(template.findFirst("lido:descriptiveMetadata/lido:objectClassificationWrap/lido:classificationWrap/lido:classification").getId());
thematicContext = cache.duplicate(template.findFirst("lido:descriptiveMetadata/lido:objectClassificationWrap/lido:classificationWrap/lido:classification").getId());
edmType = cache.duplicate(template.findFirst("lido:descriptiveMetadata/lido:objectClassificationWrap/lido:classificationWrap/lido:classification").getId());

// Cultural Heritage Type
culturalHeritageType.setLabel("classification (Cultural Heritage Type)");
culturalHeritageTypeAttr = culturalHeritageType.getAttribute("@lido:type").addConstantMapping("object classification");
culturalHeritageTypeForSkos = culturalHeritageType.findFirst("lido:conceptID");
culturalHeritageTypeForSkos.getAttribute("@lido:type").addConstantMapping("URI");
// add vocabulary

//Create thematicContext classification
thematicContext.setLabel("classification (Thematic Context)");
thematicContextTypeAttr = thematicContext.getAttribute("@lido:type").addConstantMapping("universal classification");
thematicContextForSkos = thematicContext.findFirst("lido:conceptID");
thematicContextForSkos.getAttribute("@lido:type").addConstantMapping("URI");

//Create edmType classification
edmType.setLabel("classification (Europeana)");
edmTypeAttr = thematicContext.getAttribute("@lido:type").addConstantMapping("europeana:type");
edmTypeTerm = edmType.getChild("lido:term");
edmTypeTerm.addEnumeration("IMAGE");
edmTypeTerm.addEnumeration("SOUND");
edmTypeTerm.addEnumeration("TEXT");
edmTypeTerm.addEnumeration("VIDEO");
edmTypeTerm.addEnumeration("3D");
edmTypeTerm.setMandatory(true);


// Repository
repositorySet = cache.duplicate(template.findFirst("lido:descriptiveMetadata/lido:objectIdentificationWrap/lido:repositoryWrap/lido:repositorySet").getId());
repositorySet.setLabel("repositorySet (current)");
repositoryType = repositorySet.getAttribute("@lido:type");
repositoryType.addConstantMapping("current");
repositoryType.setFixed(true);
repositoryName = repositorySet.getChild("lido:repositoryName").getChild("lido:legalBodyName");
repositoryLegalBodyID = repositorySet.getChild("lido:repositoryName").getChild("lido:legalBodyID");
repositoryWorkID = repositorySet.getChild("lido:workID");
repositoryLocation = repositorySet.getChild("lido:repositoryLocation").getChild("lido:namePlaceSet");
repositoryLocationPlaceID = repositorySet.getChild("lido:repositoryLocation").getChild("lido:placeID");
repositoryLocationPartOfPlace = repositorySet.getChild("lido:repositoryLocation").getChild("lido:partOfPlace");
repositoryLocationPartOfPlace.setLabel("partOfPlace (TGN Nation)");
repositoryLocationPartOfPlaceForSkos = repositoryLocationPartOfPlace.getChild("lido:placeID");
repositoryLocationPartOfPlaceForSkos.getAttribute("@lido:type").addConstantMapping("URI");


// Add the event types
eventTypes = template.find("//lido:eventType/lido:conceptID");
for(eventType in eventTypes) {
    eventType.setThesaurus(MappingPrimitives.thesaurus("http://terminology.lido-schema-types.org"));
}
//Original Event
event = template.findFirst("//lido:descriptiveMetadata/lido:eventWrap/lido:eventSet");

//An event
eventSet = cache.duplicate(template.findFirst("//lido:descriptiveMetadata/lido:eventWrap/lido:eventSet").getId());
eventSet.setLabel("eventSet");
eventType = eventSet.findFirst("lido:event/lido:eventType");
eventCulture = eventSet.findFirst("lido:event/lido:culture");
eventSetConceptID = eventSet.findFirst("lido:event/lido:eventType/lido:conceptID");
eventSetConceptID.getAttribute("@lido:type").addConstantMapping("URI");

eventActorOr = eventSet.findFirst("lido:event/lido:eventActor");
eventActorActorinRole = eventActorOr.getChild("lido:actorInRole");
eventActorAttributionQualifier = eventActorActorinRole.getChild("lido:attributionQualifierActor");
eventExtentActor = eventActorActorinRole.getChild("lido:extentActor");
eventActor = eventSet.findFirst("lido:event/lido:eventActor/lido:actorInRole/lido:actor");
eventActorName = eventActor.getChild("lido:nameActorSet");
eventActorForSkos = eventSet.findFirst("lido:event/lido:eventActor/lido:actorInRole/lido:actor/lido:actorID");

eventDate = eventSet.findFirst("lido:event/lido:eventDate");
eventPlace = eventSet.findFirst("lido:event/lido:eventPlace");
eventPlaceName = eventSet.findFirst("lido:event/lido:eventPlace/lido:place/lido:namePlaceSet/lido:appellationValue");

//Material and Technique
eventMaterial = cache.duplicate(eventSet.findFirst("lido:event/lido:eventMaterialsTech/lido:materialsTech/lido:termMaterialsTech").getId());
eventTechnique = cache.duplicate(eventSet.findFirst("lido:event/lido:eventMaterialsTech/lido:materialsTech/lido:termMaterialsTech").getId());


eventMaterial.getAttribute("@lido:type").addConstantMapping("material");
eventMaterialForSkos = eventMaterial.findFirst("lido:conceptID");


//PPTechnique
eventTechnique.getAttribute("@lido:type").addConstantMapping("technique");
eventTechniqueForSkos = eventTechnique.findFirst("lido:conceptID");


// Set Rights work set
recordRights = cache.duplicate(template.findFirst("//lido:administrativeMetadata/lido:recordWrap/lido:recordRights").getId());
recordRights.setLabel("recordRights (based on europeana)");
recRightsType = recordRights.getChild("lido:rightsType").getChild("lido:conceptID");
recRightsType.getAttribute("@lido:type").addConstantMapping("URI");
recRightsType.addEnumeration("http://www.europeana.eu/rights/rr-f/");
recRightsType.addEnumeration("http://www.europeana.eu/rights/rr-p/");
recRightsType.addEnumeration("http://www.europeana.eu/rights/unknown/");
recRightsType.addEnumeration("http://creativecommons.org/publicdomain/mark/1.0/");
recRightsType.addEnumeration("http://creativecommons.org/publicdomain/zero/1.0/");
recRightsType.addEnumeration("http://creativecommons.org/licenses/by/4.0/");
recRightsType.addEnumeration("http://creativecommons.org/licenses/by-sa/4.0/");
recRightsType.addEnumeration("http://creativecommons.org/licenses/by-nc/4.0/");
recRightsType.addEnumeration("http://creativecommons.org/licenses/by-nc-sa/4.0/");
recRightsType.addEnumeration("http://creativecommons.org/licenses/by-nd/4.0/");
recRightsType.addEnumeration("http://creativecommons.org/licenses/by-nc-nd/4.0/");
recRightsType.addEnumeration("http://www.europeana.eu/rights/out-of-copyright-non-commercial/");
recRightsType.addEnumeration("http://www.europeana.eu/rights/orphan-work-eu/");


// Make the recordInfoLink mandatory
recordInfoLink =  template.findFirst("//lido:recordInfoSet/lido:recordInfoLink"); //****It is not made MANDATORY

// Set europeana resource set
//resource = cache.duplicate(template.findFirst("//lido:administrativeMetadata/lido:resourceWrap/lido:resourceSet").getId());
resource = template.findFirst("//lido:administrativeMetadata/lido:resourceWrap/lido:resourceSet");
resource.setLabel("resourceSet (based on europeana)");

// 	Set rights
rights = resource.getChild("lido:rightsResource");
rights.setLabel("rightsResource (based on europeana)");
rights.setMandatory(true);

rightsTypeOr = rights.getChild("lido:rightsType");
rightsTypeOr.setLabel("rightsType (based on europeana)");
rightsType = rightsTypeOr.getChild("lido:term");
rightsType.setLabel("term (europeana)");
rightsType.getAttribute("@lido:pref").addConstantMapping("preferred");
rightsType.setMandatory(true);
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
rightsTypeEuropeana = rightsType;

// 	Set image thumb and image master
master = cache.duplicate(template.findFirst("//lido:administrativeMetadata/lido:resourceWrap/lido:resourceSet/lido:resourceRepresentation").getId());
master.setLabel("resourceRepresentation (master)");
master.setRemovable(true);
thumb = cache.duplicate(template.findFirst("//lido:administrativeMetadata/lido:resourceWrap/lido:resourceSet/lido:resourceRepresentation").getId());
thumb.setLabel("resourceRepresentation (thumb)");
thumb.setRemovable(true);

linkResource = master.getChild("lido:linkResource");
linkResource.setLabel("linkResource (master)");
linkType = master.getAttribute("@lido:type");
linkType.addConstantMapping("image_master");
linkType.setFixed(true);
linkResourceMaster = linkResource;

linkResource = thumb.getChild("lido:linkResource");
linkResource.setLabel("linkResource (thumb)");
linkType = thumb.getAttribute("@lido:type");
linkType.addConstantMapping("image_thumb");
linkType.setFixed(true);
linkResourceThumb = linkResource;


// Make the europeana data provider mandatory
originalRecordSource = template.findFirst("//lido:recordSource");
recordSource = cache.duplicate(template.findFirst("//lido:administrativeMetadata/lido:recordWrap/lido:recordSource").getId());
recordSource.setLabel("recordSource (based on europeana)");
recordSourceType = recordSource.getAttribute("@lido:type").addConstantMapping("europeana:dataProvider").setFixed(true)
recordSourceAppellation = recordSource.getChild("lido:legalBodyName").setMandatory(true);
originalRecordSource.setString(JSONMappingHandler.ELEMENT_MINOCCURS, "0");

//Actor Role
eventActorRole = eventSet.findFirst("lido:event/lido:eventActor/lido:actorInRole/lido:roleActor");
eventActorRoleForSkos = eventSet.findFirst("lido:event/lido:eventActor/lido:actorInRole/lido:roleActor/lido:conceptID");

//Vocabularies
eventActorRoleForSkos.setThesaurus(MappingPrimitives.thesaurus("http://terminology.lido-schema.org"));

thematicContextForSkos.setThesaurus(MappingPrimitives.thesaurus("http://udcdata.info/udc-schema")); // UDC 
repositoryLocationPartOfPlaceForSkos.setThesaurus(MappingPrimitives.thesaurus("http://www.tgn.gr/ConceptScheme")); // TGN 

eventLocationPartOfPlaceForSkos = eventPlace.getChild("lido:place").getChild("lido:partOfPlace").getChild("lido:placeID");
eventLocationPartOfPlaceForSkos.setThesaurus(MappingPrimitives.thesaurus("http://www.tgn.gr/ConceptScheme")); // TGN 


eventCultureForSKOS = eventCulture.getChild("lido:conceptID");
eventCultureForSKOS.setThesaurus(MappingPrimitives.thesaurus("http://www.cultural.gr/ConceptScheme")); //AAT Cultural

eventMaterialForSkos.setThesaurus(MappingPrimitives.thesaurus("http://www.materials.gr/ConceptScheme")); //AAT Materials
eventTechniqueForSkos.setThesaurus(MappingPrimitives.thesaurus("http://www.techniques.gr/ConceptScheme")); //AAT Techniques

culturalHeritageTypeForSkos.setThesaurus(MappingPrimitives.thesaurus("http://www.culturalType.gr/ConceptScheme"));

//objectWorkTypeForSkos.setThesaurus(MappingPrimitives.thesaurus("http://www.objects.gr/ConceptScheme")); // AAT Objects Facet 

//objectWorkTypeForSkos.setThesaurus(MappingPrimitives.thesaurus("http://culturaitalia.it/pico/thesaurus/4.3")); // PICO 
//culturalHeritageTypeForSkos.setThesaurus(MappingPrimitives.thesaurus("http://www.cultural.gr/ConceptScheme")); 

//http://www.cultural.gr/ConceptScheme
//http://udcdata.info/udc-schema
//http://www.materials.gr/ConceptScheme
//http://www.techniques.gr/ConceptScheme
//http://www.tgn.gr/ConceptScheme
//http://www.culturalType.gr/ConceptScheme

//PICO ?
//http://www.objects.gr/ConceptScheme

// Update nation
// Remove on object work type Done
// add the Cultural Type 




////////////////////
// Bookmarks
////////////////////
mappings.addBookmarkForXpath(" 1. Descriptive metadata language (M)", "/lido/descriptiveMetadata/@lang");
//mappings.addBookmark(" 2. Object/Work Type (M)", template.find("lido:descriptiveMetadata/lido:objectClassificationWrap/lido:objectWorkTypeWrap/lido:objectWorkType").get(1));
mappings.addBookmark(" 2. Object/Work Type AAT (Recommended use of AAT URI - M)", objectWorkType);
mappings.addBookmark(" Europeana Classification (M)", edmType);
mappings.addBookmark(" 3. Cultural Heritage Type (M)", culturalHeritageType);
mappings.addBookmark(" 4. Thematic Context (M)", thematicContext);
mappings.addBookmarkForXpath(" 5. Title (M)", "/lido/descriptiveMetadata/objectIdentificationWrap/titleWrap/titleSet/appellationValue");
mappings.addBookmark(" 6. Current Repository - Name (M/A)",  repositoryName);
mappings.addBookmark(" 6a. Current Repository - Identifier (if ULAN or VIAF URI exists M/A)",  repositoryLegalBodyID);
mappings.addBookmark(" 7. Current Repository - WorkID (R)",  repositoryWorkID);
mappings.addBookmark(" 8. Current Repository - Location (M)",  repositoryLocation);
mappings.addBookmark(" 8a. Current Repository - Location Identifier (if TGN URI exists M)",  repositoryLocationPlaceID);
mappings.addBookmark(" 8b. Current Repository - Location is part of TGN Nation (if TGN URI exists M)",  repositoryLocationPartOfPlace);  
mappings.addBookmarkForXpath(" 9. Object Description (R)","/lido/descriptiveMetadata/objectIdentificationWrap/objectDescriptionWrap/objectDescriptionSet");
mappings.addBookmarkForXpath("10. Object Measurements / Dimensions (R)", "/lido/descriptiveMetadata/objectIdentificationWrap/objectMeasurementsWrap/objectMeasurementsSet");
mappings.addBookmarkForXpath("11. Inscriptions (R)", "/lido/descriptiveMetadata/objectIdentificationWrap/inscriptionsWrap/inscriptions");
mappings.addBookmark("12. Event Set (at least one M)",eventSet); 
mappings.addBookmark("- Event Type (M)",eventType); 
mappings.addBookmark("13. Event Actor - Display Name (R)", eventActorOr);
mappings.addBookmark("14. Event Actor - Name (M/A)", eventActorName);
mappings.addBookmark("14a. Event Actor - Actor Identifier (if ULAN or VIAF URI exists M/A)", eventActor.getChild("lido:actorID"));
mappings.addBookmark("15. Event Actor - Role (R)", eventActorRole);
mappings.addBookmark("16. Event Actor - Attribution Qualifier (R)", eventActorAttributionQualifier);
mappings.addBookmark("17. Event Actor - Extent (R)", eventExtentActor);
mappings.addBookmark("18. Event Cultural Context (M/A)", eventCulture);
mappings.addBookmark("19. Event Date - Display (M/A)",  eventDate);
mappings.addBookmark("19a. Event Date - Earliest/Latest",  eventDate.getChild("lido:date"));
mappings.addBookmark("20. Event Period (M/A)",  eventSet.getChild("lido:event").getChild("lido:periodName"));
mappings.addBookmark("21. Event Place - Display (R)",  eventPlace);
mappings.addBookmark("22. Event Place - Place Name (M/A)",  eventPlaceName);
mappings.addBookmark("22a. Event Place - Place Identifier (if TGN URI exists M/A)",  eventPlace.getChild("lido:place").getChild("lido:placeID"));
mappings.addBookmark("22b. Event Place - Place is part of TGN Nation (if TGN URI exists M/A)",  eventPlace.getChild("lido:place").getChild("lido:partOfPlace"));
mappings.addBookmark("23. Event Materials/Technique - Display (R)",  eventSet.findFirst("lido:event/lido:eventMaterialsTech"));
mappings.addBookmark("24a. Event Materials - Index (M/A)",  eventMaterial);
mappings.addBookmark("24b. Event Technique - Index (M/A)",  eventTechnique);
mappings.addBookmark("25. Event Materials/Technique - Extent (R)",  eventSet.findFirst("lido:event/lido:eventMaterialsTech/lido:materialsTech/lido:extentMaterialsTech"));
mappings.addBookmark("26. Subject (R)", template.findFirst("lido:descriptiveMetadata/lido:objectRelationWrap/lido:subjectWrap/lido:subjectSet/lido:subject"));
mappings.addBookmark("27. Related Works (R)", template.findFirst("lido:descriptiveMetadata/lido:objectRelationWrap/lido:relatedWorksWrap/lido:relatedWorkSet"));
mappings.addBookmarkForXpath("28. Administrative metadata language (M)", "/lido/administrativeMetadata/@lang");
mappings.addBookmark("29. Rights Work - Credit Line (R)", template.findFirst("lido:administrativeMetadata/lido:rightsWorkWrap/lido:rightsWorkSet/lido:creditLine"));
mappings.addBookmarkForXpath("30. Record Identifier (M)", "/lido/administrativeMetadata/recordWrap/recordID");
mappings.addBookmarkForXpath("31. Record Type (M)", "/lido/administrativeMetadata/recordWrap/recordType");
mappings.addBookmarkForXpath("32. Record Source (M)", "/lido/administrativeMetadata/recordWrap/recordSource/legalBodyName");
mappings.addBookmark("33. Record Rigths (M)", recordRights.getChild("lido:rightsType")); 
mappings.addBookmark("34. Record Link (Link to metadata) (R)", recordInfoLink);
mappings.addBookmark("35. Resource Set / DCHO (at least one M)", resource);
mappings.addBookmark("36a. Link Resource / DCHO (Thumbnail) (R)",  thumb); 
mappings.addBookmark("36b. Link Resource / DCHO (Master) (M)", master);
mappings.addBookmark("37. Resource Description (R)", resource.getChild("lido:resourceDescription"));
mappings.addBookmark("38. Date Taken (R)", resource.getChild("lido:resourceDateTaken"));
mappings.addBookmark("39. Creator of the resource / photographer (R)", resource.getChild("lido:resourceSource").getChild("lido:legalBodyName"));
mappings.addBookmark("40. Rights Resource (M)", rightsTypeOr); 

handlers = template.find("//@xml:lang");
for(Element handler: handlers) {
  handler.setThesaurus(MappingPrimitives.vocabulary("http://mint.image.ece.ntua.gr/Vocabularies/Languages/LangThesaurus"));
}


