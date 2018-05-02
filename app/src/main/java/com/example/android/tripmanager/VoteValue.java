package com.example.android.tripmanager;

public enum VoteValue {

    UP_VOTE(1),
    DOWN_VOTE(-1),
    NO_VOTE(0);

    private int val;

    VoteValue(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static VoteValue getEnum(int val) {
        switch (val) {
            case 0:
                return NO_VOTE;

            case 1:
                return UP_VOTE;

            case -1:
                return DOWN_VOTE;

            default:
                return NO_VOTE;
        }
    }
}
