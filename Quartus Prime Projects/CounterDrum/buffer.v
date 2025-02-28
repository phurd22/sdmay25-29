module buffer (
    input [4:0] din,   // 5-bit input data bus
    output [4:0] dout  // 5-bit output data bus
);

    // The output is simply the input data
    assign dout = din;

endmodule
