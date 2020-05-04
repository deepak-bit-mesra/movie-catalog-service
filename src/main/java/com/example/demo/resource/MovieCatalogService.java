package com.example.demo.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import brave.Tracer;
import brave.Tracing;

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
	
	@Value("${rating.serviceName}")
	private String ratingServiceName;
	@Value("${info.serviceName}")
	private String infoServiceName;
	
	
	@Value("${rating.port}")
	private String ratingPort;
	@Value("${info.port}")
	private String infoPort;
	
	
	Logger LOGGER = LoggerFactory.getLogger(MovieCatalogService.class);
	
	

	@RequestMapping("/trace/{userId}")
	public ResponseEntity<Object> getResponseEntity(@PathVariable("userId") String userId){
		ResponseEntity<Object> responseEntity;
		CatalogItemWrapper catalogItemWrapper = new CatalogItemWrapper();
		
		try {
//			------------------------------- BUSINESS LOGIC  -------------------------------
			LOGGER.info(". . . . . . . . . . . . . . . .  . . . . . . Before rating service");
			UserRating userRating = restTemplate.getForObject("http://"+ ratingServiceName +":"+ ratingPort +"/ratingsdata/users/{userId}", UserRating.class,userId);
			LOGGER.info(". . . . . . . . . . . . . . . .  . . . . . . After Rating Service");
			List<Rating> ratings = userRating.getUserRating();
			List<CatalogItem> catalogItems=  ratings.stream().map(rating -> {
				LOGGER.info(".....................................................Before movie-info-service");
				Movie movie = restTemplate.getForObject("http://"+ infoServiceName +":"+ infoPort +"/movies/{movieId}", Movie.class,rating.getMovieId());
				LOGGER.info(".....................................................After movie-info-service");
				return new CatalogItem(movie.getName(),"Desc = "+rating.getMovieId(),rating.getRating());
			})
			.collect(Collectors.toList());
			
			catalogItemWrapper.setCatalogItems(catalogItems);
			catalogItemWrapper.setUserId(userId);
			
//			------------------------------- Adding Response Headers For Sending TraceId in Headers ------------------------
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-B3-TraceId", tracer.currentSpan().context().traceIdString());
			headers.add("X-B3-SpanId", tracer.currentSpan().context().spanIdString());
			responseEntity = ResponseEntity.status(HttpStatus.OK).headers(headers).contentType(MediaType.parseMediaType("application/json")).body(catalogItemWrapper);
			return responseEntity;
			
			
			
			
		}
		catch(Exception ex) {
			LOGGER.error(ex.getMessage());
//			------------------------------- Adding Response Headers For Sending TraceId in Headers ------------------------
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-B3-TraceId", tracer.currentSpan().context().traceIdString());
			headers.add("X-B3-SpanId", tracer.currentSpan().context().spanIdString());
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).contentType(MediaType.parseMediaType("application/json")).body(catalogItemWrapper);
			return responseEntity;
			
		}
		

			
			
			
		
	//	-----------------------------  Sending CatalogItemWrapper as Response ------------------
	//	CatalogItemWrapper catalogItemWrapper = new CatalogItemWrapper();
	//	TraceInfo traceInfo = new TraceInfo(tracer.currentSpan());
	//	traceInfo.setSpanIdStr(tracer.currentSpan().context().traceIdString());
	//	traceInfo.setTraceIdStr(tracer.currentSpan().context().spanIdString());
	//	catalogItemWrapper.setCatalogItems(catalogItems);
	//	catalogItemWrapper.setTraceInfo(traceInfo);
	//	return catalogItemWrapper;
		
	//	------------------------------  Adding Response Headers For File Download --------------------------
	//	File file = new File("filename.txt");
	//	InputStreamResource	resource = new InputStreamResource(new FileInputStream(file));
	//	HttpHeaders headers = new HttpHeaders();
	//	headers.add("Content-Disposition:",String.format("attachment; filename=\"%s\"", file.getName()));
	//	headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	//	headers.add("Pragma", "no-cache");
	//	headers.add("Expires", "0");
	//	ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
		
				
		
	
		
	}

}



