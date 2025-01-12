package com.example.radio.practic.repository.exceptions;

public class RepositoryExceptions {
    public static class EntityNotFoundException extends RuntimeException{
        public EntityNotFoundException(String message){
            super(message);
        }
    }
    public static class DuplicateIDException extends RuntimeException{
        public DuplicateIDException(String message){
            super(message);
        }
    }
    public static class TextFileException extends RuntimeException{
        public TextFileException(String message){
            super(message);
        }
    }
    public static class BinaryFileException extends RuntimeException{
        public BinaryFileException(String message){
            super(message);
        }
    }
    public static class SQLException extends RuntimeException{
        public SQLException(String message){
            super(message);
        }
    }
}