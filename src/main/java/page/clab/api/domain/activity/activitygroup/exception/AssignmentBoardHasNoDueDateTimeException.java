package page.clab.api.domain.activity.activitygroup.exception;

public class AssignmentBoardHasNoDueDateTimeException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "과제 게시판에 마감기한이 없습니다.";

    public AssignmentBoardHasNoDueDateTimeException() {
        super(DEFAULT_MESSAGE);
    }

    public AssignmentBoardHasNoDueDateTimeException(String s) {
        super(s);
    }
}

