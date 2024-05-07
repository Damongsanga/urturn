package com.ssafy.urturn.github.dto;


import lombok.Getter;

@Getter
public class GithubUploadRequestDto {
    private String message;
    private Committer committer;
    private String content;

    public GithubUploadRequestDto(String message, String name, String email , String content) {
        this.message = message;
        this.committer = new Committer(name, email);
        this.content = content;
    }

    @Getter
    private static class Committer {
        private String name;
        private String email;

        public Committer(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }
}
