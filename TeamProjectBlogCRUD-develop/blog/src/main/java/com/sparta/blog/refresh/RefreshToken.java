package com.sparta.blog.refresh;

import com.sparta.blog.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * Refresh package
 * Writer by Park
 */
@Entity
@Table(name = "refreshtoken")
@Getter
@NoArgsConstructor
public class RefreshToken extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long keys;

    private String token;

    public RefreshToken updateToken(RefreshToken token) {
        this.token = token.getToken();
        return this;
    }
//use this builder in UserService RefreshToken.buildeR()
    @Builder
    public RefreshToken(Long keys, String token) {
        this.keys = keys;
        this.token = token;
    }
}
