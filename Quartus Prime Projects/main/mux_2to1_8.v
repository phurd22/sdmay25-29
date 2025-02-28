module mux_2to1_8bit (
    input wire [7:0] bus_a,  // 8-bit input bus A
    input wire [7:0] bus_b,  // 8-bit input bus B
    input wire sel,          // Select signal (0 for bus_a, 1 for bus_b)
    output wire [7:0] bus_out // 8-bit output bus
);

// 2-to-1 multiplexer logic
assign bus_out = (sel) ? bus_b : bus_a;

endmodule