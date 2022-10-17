package fr.univlyon1.m1if.m1if03.classes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MessageTest {
    private final Message message = new Message(new User("totoLogin", "toto"), "test message");

    @Test
    void getUser() {
        assertEquals(message.getUserLogin(), "totoLogin");
    }

    @Test
    void getText() {
        assertEquals(message.getText(), "test message");
    }

    @Test
    void setText() {
        message.setText("test message 2");
        assertEquals(message.getText(), "test message 2");
    }
}
