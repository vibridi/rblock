# RBlock

Learnable blocking function for record linkage. This library and its learnable blocking algorithm is based upon Mikhail Bilenko’s Ph.D. thesis “Learnable Similarity Functions and Their Application to Record Linkage and Clustering”.
A primer about blocking in record linkage problems can be found at [this Wikipedia article](https://en.wikipedia.org/wiki/Record_linkage).


## Getting Started

RBlock is meant to be used as part of a record linkage solution.


### Installing

You can install RBlock with maven: 
1. Clone this repository.
2. Navigate to the installation folder and run: 

``` mvn clean install ```

3. Add the library as a maven dependency to your project:

```
<dependency>
	<groupId>com.vibridi</groupId>
	<artifactId>rblock</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

Requirements:
Java 8 or above.


## Customization

The core logic of the learnable blocking function is centered on instances of blocking predicates. This library provides the following generic predicates in the `com.vibridi.rblock.predicate` package:
- ExactMatch: indexes keys based on exact matches
- CommonToken: indexes keys based on common word tokens
- CommonNGram: indexes keys based on the intersection between their n-gram representations
- NCharPrefix: indexes keys based on their n-character prefix
- OffByXInteger: indexes numeric keys based on the distance X between them
- NGramTFIDF: indexes keys based on TF-IDF cosine distance between their n-gram representations

If you want to develop generic predicates for your specific problem domain, you can extend the base class `BlockingPredicate<T>`. 


## Current version

0.0.1-SNAPSHOT

This library is currently a work-in-progress, even though the main logic should already work.

## Authors

* **Gabriele Vaccari** - *Initial work* - [Vibridi](https://github.com/vibridi/)

Currently there are no other contributors

## TODOs

- implement DNF blocking
- improve the javadocs

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
