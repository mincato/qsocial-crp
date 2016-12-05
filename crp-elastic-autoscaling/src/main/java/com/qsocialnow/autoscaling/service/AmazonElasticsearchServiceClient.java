package com.qsocialnow.autoscaling.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceClient;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.PredefinedClientConfigurations;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.http.HttpResponseHandler;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.protocol.json.JsonClientMetadata;
import com.amazonaws.protocol.json.JsonErrorResponseMetadata;
import com.amazonaws.protocol.json.JsonOperationMetadata;
import com.amazonaws.protocol.json.SdkJsonProtocolFactory;
import com.amazonaws.regions.Region;
import com.amazonaws.services.elasticsearch.AWSElasticsearch;
import com.amazonaws.services.elasticsearch.model.AddTagsRequest;
import com.amazonaws.services.elasticsearch.model.AddTagsResult;
import com.amazonaws.services.elasticsearch.model.CreateElasticsearchDomainRequest;
import com.amazonaws.services.elasticsearch.model.CreateElasticsearchDomainResult;
import com.amazonaws.services.elasticsearch.model.DeleteElasticsearchDomainRequest;
import com.amazonaws.services.elasticsearch.model.DeleteElasticsearchDomainResult;
import com.amazonaws.services.elasticsearch.model.DescribeElasticsearchDomainConfigRequest;
import com.amazonaws.services.elasticsearch.model.DescribeElasticsearchDomainConfigResult;
import com.amazonaws.services.elasticsearch.model.DescribeElasticsearchDomainRequest;
import com.amazonaws.services.elasticsearch.model.DescribeElasticsearchDomainResult;
import com.amazonaws.services.elasticsearch.model.DescribeElasticsearchDomainsRequest;
import com.amazonaws.services.elasticsearch.model.DescribeElasticsearchDomainsResult;
import com.amazonaws.services.elasticsearch.model.ListDomainNamesRequest;
import com.amazonaws.services.elasticsearch.model.ListDomainNamesResult;
import com.amazonaws.services.elasticsearch.model.ListTagsRequest;
import com.amazonaws.services.elasticsearch.model.ListTagsResult;
import com.amazonaws.services.elasticsearch.model.RemoveTagsRequest;
import com.amazonaws.services.elasticsearch.model.RemoveTagsResult;
import com.amazonaws.services.elasticsearch.model.UpdateElasticsearchDomainConfigRequest;
import com.amazonaws.services.elasticsearch.model.UpdateElasticsearchDomainConfigResult;
import com.amazonaws.services.elasticsearch.model.transform.UpdateElasticsearchDomainConfigRequestMarshaller;
import com.amazonaws.services.elasticsearch.model.transform.UpdateElasticsearchDomainConfigResultJsonUnmarshaller;
import com.amazonaws.util.AWSRequestMetrics;
import com.amazonaws.util.AWSRequestMetrics.Field;
import com.qsocialnow.autoscaling.config.AWSElasticsearchConfigurationProvider;
import com.amazonaws.util.CredentialUtils;

public class AmazonElasticsearchServiceClient extends AmazonWebServiceClient implements AWSElasticsearch {

    private AWSCredentialsProvider awsCredentialsProvider;

    /** Default signing name for the service. */
    private static final String DEFAULT_SIGNING_NAME = "es";
    
    private AWSElasticsearchConfigurationProvider configurator;

    private final SdkJsonProtocolFactory protocolFactory = new SdkJsonProtocolFactory(new JsonClientMetadata()
            .withProtocolVersion("1.1").withSupportsCbor(false).withContentTypeOverride("")
            .withBaseServiceExceptionClass(com.amazonaws.services.elasticsearch.model.AWSElasticsearchException.class));

    public AmazonElasticsearchServiceClient(AWSCredentials awsCredentials) {
        this(awsCredentials, com.amazonaws.PredefinedClientConfigurations.defaultConfig());
    }

    public AmazonElasticsearchServiceClient(AWSElasticsearchConfigurationProvider configurator) {
    	super(PredefinedClientConfigurations.defaultConfig());
        this.configurator=configurator;
    	this.awsCredentialsProvider = new StaticCredentialsProvider(configurator);
        init();
    
    }

    public AmazonElasticsearchServiceClient(AWSCredentials awsCredentials, ClientConfiguration clientConfiguration) {
        super(clientConfiguration);
        this.awsCredentialsProvider = new StaticCredentialsProvider(awsCredentials);
        init();
    }

    private void init() {
        setEndpoint("https://es.us-east-1.amazonaws.com");
        setServiceNameIntern(DEFAULT_SIGNING_NAME);
    }

    @Override
    public void setEndpoint(String endpoint) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setRegion(Region region) {
        // TODO Auto-generated method stub

    }

    @Override
    public CreateElasticsearchDomainResult createElasticsearchDomain(
            CreateElasticsearchDomainRequest createElasticsearchDomainRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeleteElasticsearchDomainResult deleteElasticsearchDomain(
            DeleteElasticsearchDomainRequest deleteElasticsearchDomainRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DescribeElasticsearchDomainResult describeElasticsearchDomain(
            DescribeElasticsearchDomainRequest describeElasticsearchDomainRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DescribeElasticsearchDomainConfigResult describeElasticsearchDomainConfig(
            DescribeElasticsearchDomainConfigRequest describeElasticsearchDomainConfigRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DescribeElasticsearchDomainsResult describeElasticsearchDomains(
            DescribeElasticsearchDomainsRequest describeElasticsearchDomainsRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListDomainNamesResult listDomainNames(ListDomainNamesRequest listDomainNamesRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListTagsResult listTags(ListTagsRequest listTagsRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RemoveTagsResult removeTags(RemoveTagsRequest removeTagsRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UpdateElasticsearchDomainConfigResult updateElasticsearchDomainConfig(
            UpdateElasticsearchDomainConfigRequest updateElasticsearchDomainConfigRequest) {
        ExecutionContext executionContext = createExecutionContext(updateElasticsearchDomainConfigRequest);

        AWSRequestMetrics awsRequestMetrics = executionContext.getAwsRequestMetrics();
        awsRequestMetrics.startEvent(Field.ClientExecuteTime);
        Request<UpdateElasticsearchDomainConfigRequest> request = null;
        Response<UpdateElasticsearchDomainConfigResult> response = null;

        try {
            awsRequestMetrics.startEvent(Field.RequestMarshallTime);
            try {
                request = new UpdateElasticsearchDomainConfigRequestMarshaller(protocolFactory).marshall(super
                        .beforeMarshalling(updateElasticsearchDomainConfigRequest));
                // Binds the request metrics to the current request.
                request.setAWSRequestMetrics(awsRequestMetrics);
            } finally {
                awsRequestMetrics.endEvent(Field.RequestMarshallTime);
            }

            HttpResponseHandler<AmazonWebServiceResponse<UpdateElasticsearchDomainConfigResult>> responseHandler = protocolFactory
                    .createResponseHandler(new JsonOperationMetadata().withPayloadJson(true)
                            .withHasStreamingSuccessResponse(false),
                            new UpdateElasticsearchDomainConfigResultJsonUnmarshaller());
            response = invoke(request, responseHandler, executionContext);

            return response.getAwsResponse();

        } finally {

            endClientExecution(awsRequestMetrics, request, response);
        }
    }

    private <X, Y extends AmazonWebServiceRequest> Response<X> invoke(Request<Y> request,
            HttpResponseHandler<AmazonWebServiceResponse<X>> responseHandler, ExecutionContext executionContext) {

        executionContext.setCredentialsProvider(CredentialUtils.getCredentialsProvider(request.getOriginalRequest(),
                awsCredentialsProvider));

        return doInvoke(request, responseHandler, executionContext);
    }

    /**
     * Invoke the request using the http client. Assumes credentials (or lack
     * thereof) have been configured in the ExecutionContext beforehand.
     **/
    private <X, Y extends AmazonWebServiceRequest> Response<X> doInvoke(Request<Y> request,
            HttpResponseHandler<AmazonWebServiceResponse<X>> responseHandler, ExecutionContext executionContext) {
        request.setEndpoint(endpoint);
        request.setTimeOffset(timeOffset);

        HttpResponseHandler<AmazonServiceException> errorResponseHandler = protocolFactory
                .createErrorResponseHandler(new JsonErrorResponseMetadata());
        return client.execute(request, responseHandler, errorResponseHandler, executionContext);
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub

    }

    @Override
    public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AddTagsResult addTags(AddTagsRequest addTagsRequest) {
        // TODO Auto-generated method stub
        return null;
    }

	public AWSElasticsearchConfigurationProvider getConfigurator() {
		return configurator;
	}

}
