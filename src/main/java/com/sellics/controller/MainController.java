package com.sellics.controller;

import com.sellics.model.AmazonCompletionApi;
import com.sellics.model.SearchVolumeCalculator;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("/")
@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MainController {

    private AmazonCompletionApi amazonCompletionApi = new AmazonCompletionApi();
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private SearchVolumeCalculator searchVolumeCalculator = new SearchVolumeCalculator(amazonCompletionApi, executorService);

    @Path("/estimate")
    @GET
    public EstimateResponse estimate(@QueryParam("keyword") String keyword) {
        int score = searchVolumeCalculator.estimate(keyword);
        return new EstimateResponse(keyword, score);
    }

    @Path("/amazon")
    @GET
    public List<String> amazon(@QueryParam("keyword") String keyword) {
        return amazonCompletionApi.search(keyword);
    }

}
