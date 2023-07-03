package hellojpa;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String username;

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return username;
    }

    public void setName(final String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + username + '\'' +
                '}';
    }
}
