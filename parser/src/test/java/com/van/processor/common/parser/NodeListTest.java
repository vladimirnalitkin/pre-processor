package com.van.processor.common.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NodeListTest {
    @Test
    public void newTest() {
        NodeList<Character> test = new NodeList<>();
        assertEquals(0, test.size());
        test.print();
    }

    @Test
    public void testAdd() {
        NodeList<Character> test = new NodeList<>();
        test.addLast('a');
        test.print();
        assertEquals(1, test.size());
        test.addLast('b');
        test.print();
        assertEquals(2, test.size());
    }
}

