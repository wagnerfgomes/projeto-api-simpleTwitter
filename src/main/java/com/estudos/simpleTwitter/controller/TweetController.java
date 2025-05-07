package com.estudos.simpleTwitter.controller;

import com.estudos.simpleTwitter.controller.dto.CreateTweetDto;
import com.estudos.simpleTwitter.entity.Tweet;
import com.estudos.simpleTwitter.repository.TweetRepository;
import com.estudos.simpleTwitter.repository.UserRepository;
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
        var tweet = tweetRepository.findById(tweetId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet não encontrado!"));

        if (tweet.getUser().getUserId().equals(UUID.fromString(token.getName()))){
            tweetRepository.deleteById(tweetId);
            return new ResponseEntity<>("Tweet " + tweetId + " deletado!", HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

}