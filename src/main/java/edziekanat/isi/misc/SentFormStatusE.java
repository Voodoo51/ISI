package edziekanat.isi.misc;

public enum SentFormStatusE {
    ACCEPTED(0),
    PENDING(1),
    DECLINED(2),
    NOT_SENT(3),
    IN_NEED_OF_UPDATE(4);

    private final int value;

    SentFormStatusE(int value) {
        this.value = value;
    }

    public int getId() {
        return value;
    }
}

/*

(0, 'accepted'),
(1, 'pending'),
(2, 'declined'),
(3, 'not_sent'),
(4, 'in_need_of_update');
 */
