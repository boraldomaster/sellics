package com.sellics;

import com.sellics.model.AmazonCompletionApi;
import com.sellics.model.SearchVolumeCalculator;
import org.junit.*;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchVolumeCalculatorTest {

    private static ExecutorService executorService;

    private final AmazonCompletionApi amazonCompletionApi = Mockito.mock(AmazonCompletionApi.class);
    private SearchVolumeCalculator searchVolumeCalculator = new SearchVolumeCalculator(amazonCompletionApi, executorService);

    @BeforeClass
    public static void before() {
        executorService = Executors.newFixedThreadPool(10);
    }

    @AfterClass
    public static void after() {
        executorService.shutdown();
    }

    @Test
    public void shouldCalculate() {
        Mockito.when(amazonCompletionApi.search("a")).thenReturn(Arrays.asList("a1", "a2", "a3", "ab"));
        Mockito.when(amazonCompletionApi.search("ab")).thenReturn(Arrays.asList("ab", "ab1", "ab2"));
        int volume = searchVolumeCalculator.estimate("ab");
        Assert.assertEquals(40, volume);
    }
}
