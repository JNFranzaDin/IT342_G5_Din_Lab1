package backend.g5.dto;

public class UserResponse {
    private int userID;
    private String fullname;
    private String email;

    // Constructors
    public UserResponse() {}

    public UserResponse(int userID, String fullname, String email) {
        this.userID = userID;
        this.fullname = fullname;
        this.email = email;
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
