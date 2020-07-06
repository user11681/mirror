package user11681.mirror;

public enum TestEnum {
    ONE {
        @Override
        public int getNumber() {
            return 0x1111;
        }
    },
    TWO {
        @Override
        public int getNumber() {
            return 0x2222;
        }
    },
    THREE;

    public int getNumber() {
        return -23;
    }

    public int getSquared() {
        this.getSquared(0, 0);

        return this.getNumber() * this.getNumber();
    }

    public int getSquared(final int thing, final int other) {
        return 0;
    }

    public int getSquared(final Integer thing, final Integer other) {
        return 0;
    }

    public enum Other {
        OTHER {
            @Override
            public int other() {
                return 675342;
            }
        };

        public int other() {
            return 0;
        }
    }
}
