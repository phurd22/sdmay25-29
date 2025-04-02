module timing_memory (
    input wire [5:0] addr,  // 6-bit address bus
	 input wire clk,
    input wire ce_n,        // Active low Chip Enable
    input wire oe_n,        // Active low Output Enable
    input wire we_n,         // Active low Write Enable
	 output wire read,
	 output wire control,
	 output wire lastRead,
	 output wire lastWrite,
	 output wire clearControl,
	 output wire check,
	 output wire clearFlags,
	 output wire resetCounter_n
);

    // Memory array (64 locations of 5-bit memory)
    reg [7:0] memory [0:127]; // 2^7 = 128 locations

    // Internal data register
    reg [7:0] data_out;
	 reg [7:0] ad;

    //Initialize all memory locations to zero
    integer i;
    initial begin
        for (i = 0; i < 128; i = i + 1) begin
				if ((i % 2) != 0) begin
					memory[i] = 8'h81;
				end
				else begin
					memory[i] = 8'h80;
				end
        end
		  for (i = 100; i < 128; i = i + 1) begin
				memory[i] = memory[i] | 8'h02;
        end
		  
		  
		  memory[99] = memory[99] | 8'h04;
		  memory[98] = memory[98] | 8'h08;
		  
		  
		  memory[102] = memory[102] | 8'h10;
		  memory[104] = memory[104] | 8'h20;
		  
		  memory[106] = memory[106] | 8'h40;
		  
		  
		  memory[118] = memory[118] & 8'h7F;
		  memory[119] = memory[119] & 8'h7F;
		  
    end

    // Tri-state buffer control
    assign read = (!ce_n && !oe_n && we_n) ? data_out[0] : 1'bz;
	 assign control = (!ce_n && !oe_n && we_n) ? data_out[1] : 1'bz;
	 assign lastRead = (!ce_n && !oe_n && we_n) ? data_out[2] : 1'bz;
	 assign lastWrite = (!ce_n && !oe_n && we_n) ? data_out[3] : 1'bz;
	 assign clearControl = (!ce_n && !oe_n && we_n) ? data_out[4] : 1'bz;
	 assign check = (!ce_n && !oe_n && we_n) ? data_out[5] : 1'bz;
	 assign clearFlags = (!ce_n && !oe_n && we_n) ? data_out[6] : 1'bz;
	 assign resetCounter_n = (!ce_n && !oe_n && we_n) ? data_out[7] : 1'bz;

    // Read operation (asynchronous)
    always @(*) begin
        if (!ce_n && !oe_n && we_n) begin
				ad = (addr << 1) | clk;
            data_out <= memory[ad];
        end
    end

endmodule