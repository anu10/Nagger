package edu.ohio_state.cse.nagger;

public class User {

    private String mUserName;
    private String mEmail;

    public User(){

    }

    protected User(String username, String email){
        mUserName = username;
        mEmail = email;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getEmail() {
        return mEmail;
    }
}
