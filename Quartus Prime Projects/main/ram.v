module ram (
    input wire clk,          // Clock signal
    input wire [5:0] addr, // 16-bit address bus
    inout wire [4:0] data,  // 8-bit bidirectional data bus
    input wire ce_n,          // Active low Chip Enable
    input wire oe_n,          // Active low Output Enable
    input wire we_n           // Active low Write Enable
);

    // Memory array (64KB of 8-bit memory)
    reg [4:0] memory [0:65535];

    // Internal data register
    reg [4:0] data_out;
	 
	 // Tri-state buffer control
    assign data = (!ce_n && !oe_n && we_n) ? data_out : 8'bzzzzzzzz;

    integer i;
    initial begin
        for (i = 0; i < 65536; i = i + 1) begin
            memory[i] = 5'b0;
        end
    end

    // Write operation
    always @(posedge clk) begin
        if (!ce_n && !we_n) begin
            memory[addr] <= data;
        end
    end

    // Read operation
    always @(posedge clk) begin
        if (!ce_n && !oe_n && we_n) begin
            data_out <= memory[addr];
        end
    end

endmodule