package pl.edu.agh.io.wishlist.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

public class User {

    @Id
    private String id;

    @Indexed
    private String username;

    private String password;

    private List<String> friends;

    @DBRef
    private List<Gift> gifts;

    public User() {}

    public User(String username) {
        this(username, null);
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.friends = new ArrayList<>();
        this.gifts = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<Gift> getGifts() {
        return gifts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + username + '\'' +
                ", friends=" + friends +
                ", gifts=" + gifts +
                '}';
    }

    public String getPassword() {
        return password;
    }
}

