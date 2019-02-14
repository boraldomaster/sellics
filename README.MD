# Sellics Amazon Search Volume Estimator

## Usage
1. Build using **mvn compile**
2. Run using **mvn exec:java -Dexec.mainClass=com.sellics.Runner**

## API description

#### Estimate keyword search volume
GET http://localhost:8080/estimate?keyword=meizu
```json
{"keyword":"meizu","score":32}
```

#### Test amazon completion API
GET http://localhost:8080/amazon?keyword=meizu
```json
["meizu","meizu m6 note case","meizu 16th phone case","meizu m6 case","meizu 16th case","meizu ep51","meizu pro 7 plus","meizu pop","meizu 16th","meizu m5s case"]
```

## Algorithm

1. Find all substrings of the given keyword to estimate
2. For every substring run amazon completion API
3. Find number of keywords with higher search position
4. Find number of keywords with lower search position
5. Final score is `lower * 100 / (lower + higher)`

## Why does this algorithm give accurate score ?
Amazon completion API gives top searchable keywords that start with a given keyword.

Order matters. This can be proved by running 2 queries:
- GET http://localhost:8080/amazon?keyword=meizu
```json
["meizu","meizu m6 note case","meizu 16th phone case","meizu m6 case","meizu 16th case","meizu ep51","meizu pro 7 plus","meizu pop","meizu 16th","meizu m5s case"]
```
- GET http://localhost:8080/amazon?keyword=meiz
```json
["meiz u shaped body maternity pregnancy pillow","meiz full body pregnancy pillow","meizhi 600watt led grow light","meizu","meizhi","meizu m6 note case","meizhi 1200watt led grow light","meizitang","meiz pregnancy pillow cover","meiz pregnancy pillow"]
```
We can notice the following:
1. 2 top options from "meizu" search ("meizu", "meizu m6 note case") are still found by "meiz" search.
2. any option with lower position in "meizu" search is not found by "meiz" search
    
So, the keyword "micro sd card" that is top option even for "m" search gives a search volume of 100.
Another corner case - "samsung" - gives 0, as even "samsung" search in amazon doesn't return "samsung" keyword itself, but only various models of Samsung.   
The more keywords with lower position in amazon search the keyword has - the higher it's final search volume.