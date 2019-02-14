package com.sellics.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class AmazonCompletionApi {

    private static final String AMAZON_COMPLETION_BASE_URL =
            "http://completion.amazon.com/search/complete?search-alias=aps&client=amazon-search-ui&mkt=1";

    private ObjectMapper objectMapper = new ObjectMapper();

    private Logger log = LoggerFactory.getLogger(getClass());

    public List<String> search(String keyword) throws AmazonCompletionException {
        HttpClient client = HttpClients.createDefault();

        URI uri;
        try {
            uri = new URIBuilder(AMAZON_COMPLETION_BASE_URL)
                    .addParameter("q", keyword).build();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
        HttpGet request = new HttpGet(uri);
        HttpResponse response;
        try {
            log.info("Calling Amazon completion API for '{}' ...", keyword);
            response = client.execute(request);
            log.debug("Calling Amazon completion API for '{}' - ready!", keyword);
        } catch (IOException e) {
            throw new AmazonCompletionException("Error when calling Amazon", e);
        }
        String responseString;
        try {
            responseString = IOUtils.toString(response.getEntity().getContent(), "utf-8");
        } catch (IOException e) {
            throw new AmazonCompletionException("Error when processing Amazon response stream", e);
        }
        Object value;
        try {
            value = objectMapper.readValue(responseString, Object.class);
        } catch (IOException e) {
            throw new AmazonCompletionException("Error when parsing Amazon response string", e);
        }
        if (value instanceof List) {
            int size = ((List) value).size();
            if (size > 1) {
                Object suggestions = ((List) value).get(1);
                if (suggestions instanceof List)
                    return (List) suggestions;
            }
        }
        throw new AmazonCompletionException("Amazon response json has incorrect format");
    }

}
