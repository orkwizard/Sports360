package elastic;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;

public class ElasticConnector {
	private String host;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	private RestClient restClient;
	private RestClientTransport transport;
	private ElasticsearchClient client;
	private String key;

	
	
	public ElasticsearchClient getElasticClient() throws ElasticsearchException, IOException {
		restClient = RestClient.builder(HttpHost.create(host))
				.setDefaultHeaders(new Header[]{
					new BasicHeader("Authorization", "Basic " + key)
				})
				.setRequestConfigCallback(new RequestConfigCallback() {	
					public Builder customizeRequestConfig(Builder requestConfigBuilder) {
						// TODO Auto-generated method stub
						return requestConfigBuilder
			                    .setConnectTimeout(500000)
			                    .setSocketTimeout(8000000);
					}
				})
				.build();
		transport = new RestClientTransport(
		restClient, new JacksonJsonpMapper());

		// And create the API client
		client = new ElasticsearchClient(transport);
		try {
			System.out.println(client.ping().value());
			
			
		} catch (ElasticsearchException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return client;

	}
	
	public ElasticConnector(String host, String key) {
		super();
		this.host = host;
		this.key= key;
	}
	
	/*public ElasticConnector() {
		super();
		this.host="http://157.245.218.120:9200"; // "https://elastic.justgonow.com:9201";
		this.key= "ZWxhc3RpYzo0MXJkdzVwWGNOU2V6RjR1Mm0wWA=="; //"ZWxhc3RpYzpTeXM3M3hydjIx";
	}*/
	
	public void close() throws IOException {
		restClient.close();
	}
	

	public static void main(String[] args) throws ElasticsearchException, IOException {
		ElasticConnector elastic = new ElasticConnector("http://157.245.218.120:9200","ZWxhc3RpYzo0MXJkdzVwWGNOU2V6RjR1Mm0wWA==");
		elastic.getElasticClient();
		elastic.close();
		
	}
	
}
