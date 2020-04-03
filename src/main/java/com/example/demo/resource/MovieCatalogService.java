package com.example.demo.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.models.CatalogItem;
import com.example.demo.models.CatalogItemWrapper;
import com.example.demo.models.Movie;
import com.example.demo.models.Rating;
import com.example.demo.models.TraceInfo;
import com.example.demo.models.UserRating;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogService {
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	WebClient.Builder webClientBuilder;
	@Autowired
	Tracer tracer;
	@Autowired
	Tracing tracing;
	
	Logger LOGGER = LoggerFactory.getLogger(MovieCatalogService.class);
	
	
	
	
	
	@RequestMapping("/{userId}")
	public CatalogItemWrapper getCatalog(@PathVariable("userId") String userId){
		
		Span span =  tracer.currentSpan();
//		Span span = tracer.nextSpan().name("Just_name").annotate("Annotation");
		TraceContext traceContext = span.context();
		
		LOGGER.info(". . . . . . . . . . . . . . . .  . . . . . . Before rating service");
//		UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/{userId}", UserRating.class,userId);
//		UserRating userRating = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/{userId}", UserRating.class,userId);
		UserRating userRating = restTemplate.getForObject("http://rating:8083/ratingsdata/users/{userId}", UserRating.class,userId);
		LOGGER.info(". . . . . . . . . . . . . . . .  . . . . . . After Rating Service");
		
//		System.out.println("span.context() = " +span.context());
//		System.out.println("span in catalog = "+ span);
//		System.out.println("span.isNoop() = "+span.isNoop());
//		System.out.println("span.context().spanId() = "+span.context().spanId());
//		System.out.println("span.context().traceId() = "+span.context().traceId());
		String traceIdStr = tracer.currentSpan().context().traceIdString();
		System.out.println("tracer.currentSpan().context() = "+tracer.currentSpan().context());
		System.out.println("tracer.currentSpan().context().traceIdString() = "+traceIdStr);
		System.out.println("tracer.currentSpan().context().extra() = "+tracer.currentSpan().context().extra()); 
		System.out.println("tracer.currentSpan().context().parentIdString() = "+tracer.currentSpan().context().parentIdString());
		
		
		
		

		// Start a new trace or a span within an existing trace representing an operation
//		ScopedSpan span = tracer.startScopedSpan("encode");
		
		List<Rating> ratings = userRating.getUserRating();
		
		List<CatalogItem> catalogItems=  ratings.stream().map(rating -> {
			LOGGER.info(".....................................................Before movie-info-service");
//			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/{movieId}", Movie.class,rating.getMovieId());
//			Movie movie = restTemplate.getForObject("http://localhost:8082/movies/{movieId}", Movie.class,rating.getMovieId());
			Movie movie = restTemplate.getForObject("http://info:8082/movies/{movieId}", Movie.class,rating.getMovieId());
			LOGGER.info(".....................................................After movie-info-service");
			return new CatalogItem(movie.getName(),"Desc = "+rating.getMovieId(),rating.getRating());
		})
		.collect(Collectors.toList());
		
		CatalogItemWrapper catalogItemWrapper = new CatalogItemWrapper();
		TraceInfo traceInfo = new TraceInfo(tracer.currentSpan());
//		traceInfo.setSpanIdStr(tracer.currentSpan().context().traceIdString());
//		traceInfo.setTraceIdStr(tracer.currentSpan().context().spanIdString());
		catalogItemWrapper.setCatalogItems(catalogItems);
		catalogItemWrapper.setTraceInfo(traceInfo);
		
		return catalogItemWrapper;
		
		
	}
}





//Movie movie = webClientBuilder.build()
//				.get()
//				.uri("http://localhost:8082/movies/"+rating.getMovieId())
//				.retrieve()
//				.bodyToMono(Movie.class)
//				.block();
