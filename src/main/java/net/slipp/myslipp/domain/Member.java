package net.slipp.myslipp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue
    private Long id;

    private String name;

    private Integer age;

    //Java의 enum을 사용하려면 @Enumerated 어노테이션으로 매핑해야 한다.
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    //날짜 타입은 @Temporal을 사용해서 매핑한다.
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private  Date modifiedDate;

    //설명 필드는 길이의 제한이 없다.
    //varchar대신 CLOB 타입을 지정한다.
    //@Lob을 사용하면  CLOB, BLOB 타입을 매핑할 수 있다.
    @Lob
    private String description;
}
