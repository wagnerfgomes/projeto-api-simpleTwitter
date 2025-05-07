package com.estudos.simpleTwitter.controller;

import com.estudos.simpleTwitter.controller.dto.CreateTweetDto;
import com.estudos.simpleTwitter.entity.Tweet;
import com.estudos.simpleTwitter.repository.TweetRepository;
import com.estudos.simpleTwitter.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TweetControler {

    private TweetRepository tweetRepository;

    private UserRepository userRepository;

    public TweetControler(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/tweet")
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto dto, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));

        var tweet =  new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(dto.content());

        tweetRepository.save(tweet);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
