package page.clab.api.domain.member.exception;

public class DuplicateMemberEmailException extends RuntimeException {

    public DuplicateMemberEmailException(String s) {
        super(s);
    }

}
