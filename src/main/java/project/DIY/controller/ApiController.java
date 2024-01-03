package project.DIY.controller;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
public class ApiController {

@GetMapping("/kakaoBookSearch/{keyword}")
public String kakaoBookSearch(@PathVariable("keyword") String keyword){
        System.out.println(keyword);
        String query = keyword;
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(query);
        String encode = StandardCharsets.UTF_8.decode(buffer).toString();

        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com")
                .path("/v3/search/book")
                .queryParam("query", encode) 
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();
        System.out.println(uri);

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> req = RequestEntity
                        
                .get(uri)
                .header("Authorization", "KakaoAK 3564816811a03099a30a43ca584ef4b0")
                .build();

        ResponseEntity<String> result = restTemplate.exchange(req, String.class);
        
        return result.getBody();
}

@GetMapping("/concert/{keyword}")
public String concert(@PathVariable("keyword") String keyword){
        
		LocalDateTime now = LocalDateTime.now();
	
		int year = now.getYear();  // 연도        
		int monthValue = now.getMonthValue();  // 월(숫자)
		int dayOfMonth = now.getDayOfMonth();  // 일(월 기준)
		
		String month;
		String day;
		
		if(monthValue < 10) {
			month = "0"+ Integer.toString(monthValue);
		}else {
			month = Integer.toString(monthValue);
		}
		
		if(dayOfMonth<10) {
			day= "0"+ Integer.toString(dayOfMonth);
		}else {
			day= Integer.toString(dayOfMonth);
		}

		String toDay = Integer.toString(year) + month + day;
		System.out.println("오늘 날짜는 : " + toDay);

		if(toDay.length()!= 8) {
			toDay = "20231225";
		}
		
        String query = keyword;
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(query);
        String encode = StandardCharsets.UTF_8.decode(buffer).toString();

        URI uri = UriComponentsBuilder
                .fromUriString("http://kopis.or.kr")
                .path("/openApi/restful/pblprfr")
                .queryParam("shprfnm", encode)
                .queryParam("service", "fc7d70b374b84b11b336fd30506b763d")
                .queryParam("stdate", "20000000")
                .queryParam("stdate", toDay)
                .queryParam("cpage", "1")
                .queryParam("rows", "10")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();
        System.out.println(uri);
        System.out.println();
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> req = RequestEntity
                        
                .get(uri)
                
                .build();
        
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);
        System.out.println("공연api 호출 끝");
        return result.getBody();
}

@GetMapping("/movie/{keyword}")
public String movie(@PathVariable("keyword") String keyword){
        		
        String query = keyword;
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(query);
        String encode = StandardCharsets.UTF_8.decode(buffer).toString();
        
        System.out.println("encode: " + encode);
        
        URI uri = UriComponentsBuilder
                .fromUriString("http://api.koreafilm.or.kr")
                .path("/openapi-data2/wisenut/search_api/search_json2.jsp")
                .queryParam("collection", "kmdb_new2")
                .queryParam("detail", "Y")
                .queryParam("query", encode)
                .queryParam("startCount", 0)
                .queryParam("listCount", 10)
                
                .queryParam("ServiceKey", "4RJCFW83684G62B74F89")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();
        System.out.println(uri);

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .build();
        
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);
        System.out.println("영화api 호출 끝");
        return result.getBody();
}

}
