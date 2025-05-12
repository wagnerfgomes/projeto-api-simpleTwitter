package com.estudos.simpleTwitter.repository;

import com.estudos.simpleTwitter.entity.Tweet;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
