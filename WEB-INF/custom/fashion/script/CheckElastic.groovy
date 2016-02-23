@Grab(group='org.elasticsearch', module='elasticsearch-groovy', version='2.0.0')
@Grab(group='mysql', module='mysql-connector-java', version='5.1.38')
@GrabExclude('org.codehaus.groovy:groovy-all')

import org.elasticsearch.client.transport.TransportClient


//// Groovy way
TransportClient client = new TransportClient(ImmutableSettings.settingsBuilder {
  cluster {
	name = "catwalk"
  }
  client {
	transport {
	  sniff = true
	}
  }
})

// add transport addresses
client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9210))

SearchResponse response = client.search {
	indices "fashion6"
	types "edm-fp"
	source {
	  query {
		match {
		  _id = "*"
		}
	  }
	}
  }.actionGet()
  