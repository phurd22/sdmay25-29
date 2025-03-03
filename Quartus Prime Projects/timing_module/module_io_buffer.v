module io_buffer (
    inout [7:0] A,        // 8-bit input A
    inout [7:0] B,        // 8-bit input B
    input select         // Select line
);

assign A = (select) ? A : 8'bz;  // If select is high, A drives A_out
assign B = (select) ? 8'bz : B;  // If select is low, B drives B_out

endmodule
