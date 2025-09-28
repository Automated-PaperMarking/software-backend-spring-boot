package com.example.softwarebackend.dto;

import java.util.List;

public class TestCase {

    private String input;
    private String expectedOutput;

    public TestCase() {
        // Default constructor needed for Jackson
    }

    public TestCase(String input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    // Getters & setters
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }
}
