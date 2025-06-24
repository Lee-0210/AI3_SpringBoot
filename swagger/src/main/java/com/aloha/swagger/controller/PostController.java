package com.aloha.swagger.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.swagger.dto.Posts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostController {


    @GetMapping()
    public ResponseEntity<?> getAll() {
        try {
            List<Posts> list = new ArrayList<>();
            Posts post1 = Posts.builder().no(1l).title("title1").writer("writer1").content("content1").build();
            Posts post2 = Posts.builder().no(2l).title("title2").writer("writer2").content("content2").build();
            Posts post3 = Posts.builder().no(3l).title("title3").writer("writer3").content("content3").build();
            list.add(post1);
            list.add(post2);
            list.add(post3);

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{no}")
    public ResponseEntity<?> getOne(@PathVariable("no") Long no) {
        try {
            Posts post = Posts.builder().no(3l).title("title3").writer("writer3").content("content3").build();
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Posts post) {
        try {
            log.info("post : {}", post);
            post.setId(UUID.randomUUID().toString());
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody Posts post) {
        try {
            log.info("post : {}", post);
            return new ResponseEntity<>("Update Result", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{no}")
    public ResponseEntity<?> destroy(@PathVariable("no") Long no) {
        try {
            log.info("no : {}", no);
            return new ResponseEntity<>("Destroy Result", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
