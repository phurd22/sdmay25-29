module four_bit_counter (
 input wire clk,      // Clock signal
    input wire reset,    // Master reset (active high)
    output wire count_0, // LSB of the counter
    output wire count_1, // Second bit of the counter
    output wire count_2, // Third bit of the counter
    output wire count_3  // MSB of the counter
);

    // Internal 4-bit counter register
    reg [3:0] count;

    // Assign individual bits to output wires
    assign count_0 = count[0];
    assign count_1 = count[1];
    assign count_2 = count[2];
    assign count_3 = count[3];

    // Counter logic
    always @(posedge reset or posedge clk) begin
        if (reset) begin
            // Reset the counter to 0
            count <= 4'b0000;
        end else begin
            // Increment the counter
            count <= count + 1;
        end
    end

endmodule