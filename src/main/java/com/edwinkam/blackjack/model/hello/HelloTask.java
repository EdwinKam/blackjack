package com.edwinkam.blackjack.model.hello;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hello")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelloTask {
    @Id
    private Integer id;
    private String name;
}
