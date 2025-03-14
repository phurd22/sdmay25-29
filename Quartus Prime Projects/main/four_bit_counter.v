module four_bit_counter (
    input wire rst_n,        // Master Reset (active low, synchronous)
    input wire clk,        // Clock Pulse (positive edge triggered)
    input wire spe_n,        // Parallel Enable (active low)
    input wire te,        // Count Enable (active high)
    input wire [3:0] P,   // Parallel Data Inputs (P0-P3)
	 output wire Q0,
	 output wire Q1,
	 output wire Q2,
	 output wire Q3,
    output wire TC        // Terminal Count (active high)
);

    // Internal signals
    reg [3:0] count;      // Internal counter register

    // Synchronous reset and counting logic
    always @(posedge clk) begin
        if (!rst_n) begin
            count <= 4'b0000; // Synchronous reset (active low)
        end else if (!spe_n) begin
            count <= P;       // Parallel load (active low)
        end else if (te) begin
            count <= count + 1; // Increment counter if TE is high
        end
    end

    // Assign counter outputs
	 assign Q0 = count[0];
	 assign Q1 = count[1];
	 assign Q2 = count[2];
	 assign Q3 = count[3];

    // Terminal Count (TC) logic
    assign TC = (count == 4'b1111) && te; // TC is high when count is 15 and TE is high

endmodule