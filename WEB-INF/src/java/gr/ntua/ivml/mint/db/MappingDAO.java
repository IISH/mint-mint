package gr.ntua.ivml.mint.db;

import gr.ntua.ivml.mint.persistent.Mapping;
import gr.ntua.ivml.mint.persistent.Organization;

import java.util.List;

public class MappingDAO extends DAO<Mapping, Long> {
	
	/**
	 * Get all Mappings for that organization, even if organization is null.
	 * @param org
	 * @return
	 */
	public List<Mapping> findByOrganization( Organization org ) {
		if( org != null )
		return getSession().createQuery("from Mapping where organization=:org or shared=true order by organization, last_modified desc")
		.setEntity("org", org)
		.list();
		else 
			return getSession().createQuery("from Mapping where organization is null or shared is true order by organization, last_modified desc")
			.list();
	}
	
	public List<Mapping> findAllOrderOrg(  ) {
		
		return getSession().createQuery("from Mapping order by organization asc")
		
		.list();
		}
}
