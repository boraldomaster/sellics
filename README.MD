# Sellics Amazon Search Volume Estimator

## Usage
1. Build using **mvn compile**
2. Run using **mvn exec:java -Dexec.mainClass=com.sellics.Runner**

## API description

#### Estimate keyword search volume
```
GET http://localhost:8080/estimate?keyword=meizu
```
```json
{"keyword":"meizu","score":32}
```

#### Test amazon completion API
```
GET http://localhost:8080/amazon?keyword=meizu
```
```json
["meizu","meizu m6 note case","meizu 16th phone case","meizu m6 case","meizu 16th case","meizu ep51","meizu pro 7 plus","meizu pop","meizu 16th","meizu m5s case"]
```

## Algorithm

1. Find all substrings of given keyword to estimate
2. For every substring run amazon completion API
3. Find number of keyword with higher search rank
4. Find number of keyword with lower search rank
5. Final score is `lower * 100 / (lower + higher)`

## Why this algorithm gives accurate score ?
Amazon completion API gives top searchable keyword that start with a given keyword.

Order matters. This can be proved by trying 2 queries:
- GET http://localhost:8080/amazon?keyword=meizu
```json
["meizu","meizu m6 note case","meizu 16th phone case","meizu m6 case","meizu 16th case","meizu ep51","meizu pro 7 plus","meizu pop","meizu 16th","meizu m5s case"]
```
- GET http://localhost:8080/amazon?keyword=meiz
```json
["meiz u shaped body maternity pregnancy pillow","meiz full body pregnancy pillow","meizhi 600watt led grow light","meizu","meizhi","meizu m6 note case","meizhi 1200watt led grow light","meizitang","meiz pregnancy pillow cover","meiz pregnancy pillow"]
```
We can notice the following:
1. 2 top options from "meizu" search are still present in "meiz" search 
2. any options with lower place in "meizu" search are not found by "meiz" search
    
So, given this algorithm the keyword "micro sd card" that is top option even for "m" search has a search volume of 100.
Another corner case - "samsung" - gives 0, as even "samsung" search in amazon doesn't return "samsung" keyword itself, but only various models of Samsung.   
The more keywords with lower rank in amazon search the keyword has - the higher it's final search volume.