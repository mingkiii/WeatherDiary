package zerobase.weather.error;

public class Nonexistent extends RuntimeException{
    private static final String MESSAGE = "해당 날짜에 일기가 없습니다.";
    public Nonexistent() {
        super(MESSAGE);
    }
}
