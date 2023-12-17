package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 20)
    private String name;
    private Integer age;

    @OneToMany (mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLoanHistory> userLoanHistories = new ArrayList<>();

    protected User() {}

    public User(String name, Integer age) {
        if(name==null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다", name));
        }
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void loanBook(String bookName) {
        this.userLoanHistories.add(new UserLoanHistory(this, bookName));
    }

    public void returnBook(String bookName) {
        // 함수형 프로그래ㅐ밍 할 수 있게 Stream 시작
        UserLoanHistory targetHistory = this.userLoanHistories.stream()
                // 들어오는 객체 중에 조건에 맞는 것만 필터링 (UserLoanHistory 중 책 이름이 bookName과 같은것)
                .filter(history -> history.getBookName().equals(bookName))
                // 첫번째 값을 찾고, 없을 경우 예외를 던짐 (Optional 제거하기 위해!)
                .findFirst().orElseThrow(IllegalArgumentException::new);
        // UserLoanHistory를 반납 처리
        targetHistory.doReturn();
    }

}
