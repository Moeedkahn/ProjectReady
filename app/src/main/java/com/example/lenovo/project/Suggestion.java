package com.example.lenovo.project;

public class Suggestion {

    public String suggestion;
    public String useremail;

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public Suggestion(String suggestion, String useremail) {

        this.suggestion = suggestion;
        this.useremail = useremail;
    }

    public Suggestion() {
        suggestion=null;
        useremail=null;
    }

}
