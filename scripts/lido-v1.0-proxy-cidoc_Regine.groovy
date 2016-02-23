event = template.findFirst("//lido:descriptiveMetadata/lido:eventWrap/lido:eventSet");

eventSetProduction = cache.duplicate(template.findFirst("//lido:descriptiveMetadata/lido:eventWrap/lido:eventSet").getId());
eventSetProduction.setLabel("eventSet (Production)");
eventSetConceptID = eventSetProduction.findFirst("lido:event/lido:eventType/lido:conceptID");
eventSetConceptID.addConstantMapping("http://terminology.lido-schema.org/lido00007");
eventSetConceptID.getAttribute("@lido:type").addConstantMapping("URI");
eventSetProduction.setRemovable(true);


eventPrAuthor = eventSetProduction.findFirst("lido:event/lido:eventActor/lido:actorInRole/lido:actor");
eventPrAuthorForSkos = eventSetProduction.findFirst("lido:event/lido:eventActor/lido:actorInRole/lido:actor/lido:actorID");
eventPrDate = eventSetProduction.findFirst("lido:event/lido:eventDate/lido:date");
eventPrPlace = eventSetProduction.findFirst("lido:event/lido:eventPlace/lido:place");
eventPrPlaceName = eventSetProduction.findFirst("lido:event/lido:eventPlace/lido:place/lido:namePlaceSet/lido:appellationValue");

//Actor Role
eventPrAuthorRole = eventSetProduction.findFirst("lido:event/lido:eventActor/lido:actorInRole/lido:roleActor");
eventPrAuthorRoleForSkos = eventSetProduction.findFirst("lido:event/lido:eventActor/lido:actorInRole/lido:roleActor/lido:conceptID");
eventPrAuthorRoleForSkos.setThesaurus(MappingPrimitives.thesaurus("http://terminology.lido-schema.org"));

// Add the event types
eventTypes = template.find("//lido:eventType/lido:conceptID");
for(eventType in eventTypes) {
    eventType.setThesaurus(MappingPrimitives.thesaurus("http://terminology.lido-schema-types.org"));
}


//PPMaterial
eventPrMaterial = eventSetProduction.findFirst("lido:event/lido:eventMaterialsTech/lido:materialsTech/lido:termMaterialsTech");
eventPrMaterialForSkos = eventPrMaterial.findFirst("lido:conceptID");
eventPrMaterialForSkos.setThesaurus(MappingPrimitives.thesaurus("http://partage.vocnet.org/Materials"));

//PPTechnique
//eventPrTechnique = eventSetProduction.findFirst("lido:event/lido:eventMaterialsTech/lido:materialsTech/lido:termMaterialsTech");
//eventPrTechnique.getAttribute("@lido:type").addConstantMapping("technique");
//eventPrTechniqueForSkos = eventPrTechnique.findFirst("lido:conceptID");
//eventPrTechniqueForSkos.setThesaurus(MappingPrimitives.thesaurus("http://partage.vocnet.org/Activities"));


//Actor Role
eventPrAuthorRoleForSkos = eventSetProduction.findFirst("lido:event/lido:eventActor/lido:actorInRole/lido:roleActor/lido:conceptID");
eventPrAuthorRoleForSkos.setThesaurus(MappingPrimitives.thesaurus("http://terminology.lido-schema.org"));



////////////////////
// Bookmarks
////////////////////
mappings.addBookmarkForXpath("Descriptive metadata language", "/lido/descriptiveMetadata/@lang");
mappings.addBookmarkForXpath("Administrative metadata language", "/lido/administrativeMetadata/@lang");
mappings.addBookmarkForXpath("Object/Work Type", "/lido/descriptiveMetadata/objectClassificationWrap/objectWorkTypeWrap/objectWorkType");
mappings.addBookmarkForXpath("Object Title/Name", "/lido/descriptiveMetadata/objectIdentificationWrap/titleWrap/titleSet");
mappings.addBookmarkForXpath("Repository", "/lido/descriptiveMetadata/objectIdentificationWrap/repositoryWrap/repositorySet");
mappings.addBookmarkForXpath("Object Description","/lido/descriptiveMetadata/objectIdentificationWrap/objectDescriptionWrap/objectDescriptionSet");
mappings.addBookmarkForXpath("Dimensions", "/lido/descriptiveMetadata/objectIdentificationWrap/objectMeasurementsWrap/objectMeasurementsSet");
 
mappings.addBookmark("Production Event",eventSetProduction); 
mappings.addBookmark("- Producer", eventPrAuthor);
mappings.addBookmark("- Producer's role", eventPrAuthorRole);
mappings.addBookmark("- Production Date",  eventPrDate);
mappings.addBookmark("- Production Place",  eventPrPlace);
mappings.addBookmark("- Material/Technique",  eventPrMaterial); 
mappings.addBookmark("Other Event (type to specify)", event); 

mappings.addBookmarkForXpath("Subject / Theme (Concept)", "/lido/descriptiveMetadata/objectRelationWrap/subjectWrap/subjectSet/subject"); 
//mappings.addBookmark("Subject / Theme (Concept) (EuPhoto vocabulary)", subject);  
//mappings.addBookmarkForXpath("Rights Information for Work", "/lido/administrativeMetadata/rightsWorkWrap/rightsWorkSet");  

mappings.addBookmarkForXpath("Record Identifier", "/lido/administrativeMetadata/recordWrap/recordID");
mappings.addBookmarkForXpath("Record Type", "/lido/administrativeMetadata/recordWrap/recordType"); 
mappings.addBookmarkForXpath("Record Source (Name)", "/lido/administrativeMetadata/recordWrap/recordSource/legalBodyName/appellationValue"); 
mappings.addBookmarkForXpath("Record Link (Link to metadata)", "/lido/administrativeMetadata/recordWrap/recordInfoSet/recordInfoLink");
mappings.addBookmarkForXpath("Resource Set", "/lido/administrativeMetadata/resourceWrap/resourceSet")

handlers = template.find("//@xml:lang");
for(Element handler: handlers) {
  handler.setThesaurus(MappingPrimitives.vocabulary("http://mint.image.ece.ntua.gr/Vocabularies/Languages/LangThesaurus"));
}