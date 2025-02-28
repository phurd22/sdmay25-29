module demux_3to8 (
    input wire in,          // 1-bit input
    input wire [2:0] sel,    // 3-bit select signal
    output wire out0,       // Output 0
    output wire out1,       // Output 1
    output wire out2,       // Output 2
    output wire out3,       // Output 3
    output wire out4,       // Output 4
    output wire out5,       // Output 5
    output wire out6,       // Output 6
    output wire out7        // Output 7
);

// Internal signals for outputs
reg out0_reg, out1_reg, out2_reg, out3_reg, out4_reg, out5_reg, out6_reg, out7_reg;

// Assign the internal registers to the output wires
assign out0 = out0_reg;
assign out1 = out1_reg;
assign out2 = out2_reg;
assign out3 = out3_reg;
assign out4 = out4_reg;
assign out5 = out5_reg;
assign out6 = out6_reg;
assign out7 = out7_reg;

// Demultiplexer logic
always @(*) begin
    // Default all outputs to 0
    out0_reg = 1'b0;
    out1_reg = 1'b0;
    out2_reg = 1'b0;
    out3_reg = 1'b0;
    out4_reg = 1'b0;
    out5_reg = 1'b0;
    out6_reg = 1'b0;
    out7_reg = 1'b0;

    // Route the input to the selected output
    case (sel)
        3'b000: out0_reg = in;
        3'b001: out1_reg = in;
        3'b010: out2_reg = in;
        3'b011: out3_reg = in;
        3'b100: out4_reg = in;
        3'b101: out5_reg = in;
        3'b110: out6_reg = in;
        3'b111: out7_reg = in;
        default: begin
            out0_reg = 1'b0;
            out1_reg = 1'b0;
            out2_reg = 1'b0;
            out3_reg = 1'b0;
            out4_reg = 1'b0;
            out5_reg = 1'b0;
            out6_reg = 1'b0;
            out7_reg = 1'b0;
        end
    endcase
end

endmodule