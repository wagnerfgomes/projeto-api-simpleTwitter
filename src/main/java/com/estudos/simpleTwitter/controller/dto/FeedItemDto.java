package com.estudos.simpleTwitter.controller.dto;

public record FeedItemDto(long tweetId,
                          String content,
                          String userName) {
}
