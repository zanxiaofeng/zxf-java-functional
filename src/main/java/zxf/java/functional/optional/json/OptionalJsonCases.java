package zxf.java.functional.optional.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import zxf.java.functional.optional.model.Book;

import java.io.IOException;
import java.util.Optional;

public class OptionalJsonCases {
    public static void main(String[] args) throws IOException {
        Book book = new Book();
        book.setTitle("Test book");
        book.setSubTitle(Optional.of("this is sub title"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        String json = mapper.writeValueAsString(book);
        System.out.println(json);
        Book newBook = mapper.readValue(json, Book.class);
    }
}
