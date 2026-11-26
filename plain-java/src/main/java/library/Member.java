package library;

import java.io.Serializable;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;
    private String memberId;
    private String name;
    private String email;
    private String contactNumber;

    public Member(String memberId, String name, String email,
                  String contactNumber) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getContactNumber() { return contactNumber; }
}
