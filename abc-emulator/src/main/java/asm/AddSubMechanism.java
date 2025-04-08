package src.main.java.asm;

public class AddSubMechanism {

    // For the result of a 1-bit add or subtract
    public static class Result {
        public boolean sum;             // Result of 1-bit sum (0: False or 1: True)
        public boolean carryOut;        //The carry/borrow (0: False or 1: True)

        public Result(boolean sum, boolean carryOut) {
            this.sum = sum;
            this.carryOut = carryOut;
        }

        @Override
        public String toString() {
            return "sum: " + (sum ? "1" : "0") + ", carry: " + (carryOut ? "1" : "0");
        }
    }

    /**
     *
     * @param ca            Bit from the CA drum    false: 0, true: 1
     * @param ka            Bit from the KA drum    false: 0, true: 1
     * @param carryIn       Carry bit               false: 0, true: 1
     * @param subtract      If true, subtract, otherwise, add
     * @return              A Result object with 1-bit sum and carry-out
     */
    public static Result addSub(boolean ca, boolean ka, boolean carryIn, boolean subtract) {
        boolean sum;
        boolean carryOut;
        if (!subtract) {
            sum = ca ^ ka ^ carryIn;
            carryOut = (ca && ka) || (carryIn && (ca ^ ka));
        } else {
            sum = (!ka && !carryIn)
                    || (!ca && (ka ^ carryIn))
                    || (ca && ka && carryIn);

            carryOut = (!ca) || (ca && ka && carryIn);
        }
        return new Result(sum, carryOut);
    }
}
