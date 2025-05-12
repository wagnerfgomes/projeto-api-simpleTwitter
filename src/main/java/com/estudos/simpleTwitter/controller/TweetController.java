package com.estudos.simpleTwitter.controller;

import com.estudos.simpleTwitter.controller.dto.CreateTweetDto;
import com.estudos.simpleTwitter.controller.dto.FeedDto;
import com.estudos.simpleTwitter.controller.dto.FeedItemDto;
import com.estudos.simpleTwitter.entity.Role;
import com.estudos.simpleTwitter.entity.Tweet;
import com.estudos.simpleTwitter.repository.TweetRepository;
import com.estudos.simpleTwitter.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetController(TweetRepository tweetRepository,
                           UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }


    @PostMapping("/tweet")
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto dto,
                                            JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));

        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(dto.content());

        tweetRepository.save(tweet);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tweet/{id}")
    public ResponseEntity<String> deleteTweet(@PathVariable("id") Long tweetId, JwtAuthenticationToken token){

        var user = userRepository.findById(UUID.fromString(token.getName()));
        var tweet = tweetRepository.findById(tweetId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet não encontrado!"));

        var isADM = user.get().getRoles()
                .stream().anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));
        if (isADM || tweet.getUser().getUserId().equals(UUID.fromString(token.getName())) ){
            tweetRepository.deleteById(tweetId);
            return new ResponseEntity<>("Tweet " + tweetId + " deletado!", HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value= "page", defaultValue = "0") int page,
                                        @RequestParam(value= "pagesize", defaultValue = "10") int pageSize){
       var tweets = tweetRepository.findAll(
               PageRequest.of(page, pageSize, Sort.Direction.DESC, "createdTimeStamp" ))
               .map(tweet -> new FeedItemDto(tweet.getTweetId(), tweet.getContent(), tweet.getUser().getUserName()));
        return ResponseEntity.ok(new FeedDto(tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getTotalElements()));
    };

}