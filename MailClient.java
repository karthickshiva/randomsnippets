//User.java
package com.anu.emailclient;
class User {
    private int id;
    private String email, name;
    
    public User(int id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
}

//Mail.java
package com.anu.emailclient;
class Mail {
    //message, from, to, subject, date, cc, bcc, id
    private String from, to, subject, message;
    //constructor, getters
    public void printMail() {
        System.out.println("from : " + from);
        System.out.println("to : " + from);
        System.out.println("subject : " + from);
        System.out.println("message : " + from);
    }
}


//LoginManager.java
class LoginManager {
    static int currentId = 0;
    static Map<Integer, String> userIdVsPassword = new HashMap<>();
    static Map<Integer, User> userIdVsUser = new HashMap<>();
    public static Map<String, User> userEmailVsUser = new HashMap<>();
    
    public static User signup(String name, String email, String password) {
        User user = new User(currentId++, name, email);
        userIdVsPassword.put(user.getId(), password);
        userIdVsUser.put(user.getId(), user);
        userEmailVsUser.put(email, user);
        return user;
    }
    
    public static User login(String email, String password) throws Exception {
        User user = userEmailVsUser.get(email);
        String actualPassword = userIdVsPassword.get(user.getId());
        if(password.equals(actualPassword)) {
            return user;
        }
        throw new Exception("Check your username or password.")
    }
}

class MailClient {
    static Map<Integer, Map<Integer, Mail>> userIdVsSentMails = new HashMap<>();
    static Map<Integer, Map<Integer, Mail>> userIdVsInboxMails = new HashMap<>();
    private User user;
    
    public MailClient(String email, String password) {
        try {
            user = LoginManager.login(email, password);
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        if(!userIdVsSentMails.containsKey(user.getId())) {
            userIdVsSentMails.put(user.getId(), new HashMap<>());
        }
        if(!userIdVsInboxMails.containsKey(user.getId())) {
            userIdVsInboxMails.put(user.getId(), new HashMap<>());
        }
    }
    
    public void sendMail(String toAddress, String subject, String message) {
        
        Mail mail = new Mail(user.getEmail(), toAddress, subject, message);
        if(!LoginManager.userEmailVsUser.containsKey(toAddress)) {
            System.out.println("Invalid toAddress.");
            return;
        }
        User toUser = LoginManager.userEmailVsUser.get(toAddress);
        userIdVsSentMails.get(user.getId()).put(mail.getId(), mail);
        userIdVsInboxMails.get(toUser.getId()).put(mail.getId(), mail);
        System.out.println("Message sent.");
    }
    
    public void listInboxMails() {
        if(userIdVsInboxMails.get(user.getId()).size() == 0) {
            System.out.println("No mails found.");
            return;
        }
        for(Integer mailId : userIdVsInboxMails.get(user.getId()).keySet()) {
            Mail mail = userIdVsInboxMails.get(user.getId()).get(mailId);
            mail.printMail();
        }
    }
    
    public void listSentMails() {
        if(userIdVsSentMails.get(user.getId()).size() == 0) {
            System.out.println("No mails found.");
            return;
        }
        for(Integer mailId : userIdVsSentMails.get(user.getId()).keySet()) {
            Mail mail = userIdVsSentMails.get(user.getId()).get(mailId);
            mail.printMail();
        }
    }
    
    public void deleteInboxMail(int id) {
        if(userIdVsInboxMails.get(user.getId()).containsKey(id)) {
            userIdVsInboxMails.get(user.getId()).remove(id);
        }
        else {
            System.out.println("Invalid mail id.")
        }
    }
    
    public void deleteSentMail(int id) {
        if(userIdVsSentMails.get(user.getId()).containsKey(id)) {
            userIdVsSentMails.get(user.getId()).remove(id);
        }
        else {
            System.out.println("Invalid mail id.")
        }
    }
    
    public static void main(String[] args) {
        //Signup
        LoginManager.signup("Karthick", "karthick@abc.com", "123");
        LoginManager.signup("Anusuya", "anu@abc.com", "abc");
        
        //Login (Karthick)
        MailClient mailClient = new MailClient("karthick@abc.com", "123");
        mailClient.sendMail("anu@abc.com", "Vacation", "I will be on leave for 10 days");
        mailClient.listSentMails();
        mailClient.deleteSentMail(0);
        
        //Login (Anu)
        mailClient = new MailClient("anu@abc.com", "abc");
        mailClient.listInboxMails();
        mailClient.deleteInboxMail(0);
        mailClient.listInboxMails();
    }
}
