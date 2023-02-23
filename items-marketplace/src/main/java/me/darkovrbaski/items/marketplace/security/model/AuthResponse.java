package me.darkovrbaski.items.marketplace.security.model;

import java.util.Date;

public record AuthResponse(

    String username,

    String token,

    Date expiresIn

) {

}
