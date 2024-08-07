package page.clab.api.domain.activity.activitygroup.exception;

public class FeedbackBoardHasNoContentException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "피드백 게시판에 내용이 없습니다.";

    public FeedbackBoardHasNoContentException() {
        super(DEFAULT_MESSAGE);
    }

    public FeedbackBoardHasNoContentException(String s) {
        super(s);
    }
}