package com.sellics.model;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SearchVolumeCalculator {

    private AmazonCompletionApi amazonCompletionApi;
    private ExecutorService executorService;

    public SearchVolumeCalculator(AmazonCompletionApi amazonCompletionApi, ExecutorService executorService) {
        this.amazonCompletionApi = amazonCompletionApi;
        this.executorService = executorService;
    }


    public int estimate(String keyword) {
        Set<String> lower = ConcurrentHashMap.newKeySet();
        Set<String> higher = ConcurrentHashMap.newKeySet();
        List<Future> futures = IntStream.range(0, keyword.length()).mapToObj(i -> {
            String substring = keyword.substring(0, i + 1);
            return executorService.submit(() -> {
                List<String> suggestions = amazonCompletionApi.search(substring);
                Set<String> set = higher;
                for (String suggestion : suggestions) {
                    if (suggestion.equals(keyword))
                        set = lower;
                    else
                        set.add(suggestion);
                }
            });
        }).collect(Collectors.toList());
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException e) {
                throw new SearchVolumeCalculatorException("Execution interrupted");
            } catch (ExecutionException e) {
                throw new SearchVolumeCalculatorException("Execution failed", e.getCause());
            }
        });
        int higherSize = higher.size();
        int lowerSize = lower.size();
        return lowerSize * 100 / (lowerSize + higherSize);
    }

}

