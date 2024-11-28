package com.rocketseat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class URLData {
    private String originalURL;
    private long expirationTime;
}
