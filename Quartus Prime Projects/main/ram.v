module ram (
    input wire [5:0] addr,  // 6-bit address bus
    inout wire [4:0] data,  // 5-bit bidirectional data bus
    input wire ce_n,        // Active low Chip Enable
    input wire oe_n,        // Active low Output Enable
    input wire we_n         // Active low Write Enable
);

    // Memory array (64 locations of 5-bit memory)
    reg [4:0] memory [0:63]; // 2^6 = 64 locations

    // Internal data register
    reg [4:0] data_out;

    // Initialize all memory locations to zero
    //integer i;
    //initial begin
      //  for (i = 0; i < 64; i = i + 1) begin
        //    memory[i] = 5'b0;
        //end
    //end

    // Tri-state buffer control
    assign data = (!ce_n && !oe_n && we_n) ? data_out : 5'bzzzzz;

    // Write operation (asynchronous)
    always @(*) begin
        if (!ce_n && !we_n) begin
            memory[addr] <= data;
        end
    end

    // Read operation (asynchronous)
    always @(*) begin
        if (!ce_n && !oe_n && we_n) begin
            data_out <= memory[addr];
        end
    end

endmodule