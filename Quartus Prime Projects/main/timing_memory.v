module timing_memory (
    input wire [5:0] addr,  // 6-bit address bus
	 input wire clk,
    input wire ce_n,        // Active low Chip Enable
    input wire oe_n,        // Active low Output Enable
    input wire we_n,         // Active low Write Enable
	 output wire read,
	 output wire write,
	 output wire lastRead,
	 output wire lastWrite,
	 output wire checkInputs,
	 output wire clearCarry,
	 output wire resetCounter_n
);

    // Memory array (64 locations of 5-bit memory)
    reg [6:0] memory [0:127]; // 2^7 = 128 locations

    // Internal data register
    reg [6:0] data_out;
	 reg [6:0] ad;

    //Initialize all memory locations to zero
    integer i;
    initial begin
        for (i = 0; i < 128; i = i + 1) begin
				if ((i % 2) == 0) begin
					memory[i] = 7'b1000010;
				end
				else begin
					memory[i] = 7'b1000001;
				end
        end
		  
		  
		  memory[99] = 7'b1000101;
		  memory[98] = 7'b1001110;
		  
		  memory[101] = 7'b1000101;
		  
		  memory[105] = 7'b1010001;
		  memory[104] = 7'b1010010;
		  
		  memory[107] = 7'b1010001;
		  
		  memory[110] = 7'b1100010;
		  
		  memory[118] = 7'b0000000;
		  memory[119] = 7'b0000000;
		  
    end

    // Tri-state buffer control
    assign read = (!ce_n && !oe_n && we_n) ? data_out[0] : 1'bz;
	 assign write = (!ce_n && !oe_n && we_n) ? data_out[1] : 1'bz;
	 assign lastRead = (!ce_n && !oe_n && we_n) ? data_out[2] : 1'bz;
	 assign lastWrite = (!ce_n && !oe_n && we_n) ? data_out[3] : 1'bz;
	 assign checkInputs = (!ce_n && !oe_n && we_n) ? data_out[4] : 1'bz;
	 assign clearCarry = (!ce_n && !oe_n && we_n) ? data_out[5] : 1'bz;
	 assign resetCounter_n = (!ce_n && !oe_n && we_n) ? data_out[6] : 1'bz;


    // Read operation (asynchronous)
    always @(*) begin
        if (!ce_n && !oe_n && we_n) begin
				ad = (addr << 1) | clk;
            data_out <= memory[ad];
        end
    end

endmodule