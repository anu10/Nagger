package edu.ohio_state.cse.nagger;

public class UserManager extends User {

    private static User mUser;
    public User createUser(String mUserName, String mEmail){
        if(mUser == null) {
            mUser = new User(mUserName, mEmail);
        }
        return mUser;
    }

    public static User getUser() {
        return mUser;
    }
}
