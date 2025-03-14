module eeprom(
    input wire [3:0] digit,
    input wire [3:0] decade,
    input wire [5:0] time_slot,
    output wire data,
    input wire ce_n,
    input wire oe_n,
    input wire we_n
);

    reg memory [0:16383]; // 2^14 = 16384 locations
    reg data_out;
    reg [13:0] addr;

    integer i, j, k, num, b;

    // Initialize memory
    initial begin
        for (i = 0; i < 16384; i = i + 1) begin
            memory[i] = 1;
        end

        for (i = 1; i < 10; i = i + 1) begin
            for (j = 0; j < 15; j = j + 1) begin
                for (k = 0; k < 50; k = k + 1) begin
                    num = (((i << 4) | j) << 6) | k;
                    b = 0;

                    if (((i*(10 ** j)) & (1 << k)) == (1 << k)) begin
                        b = 1;
                    end

                    memory[num] = b;
                end
            end
        end

        for (i = 14'h3c00; i < 16384; i = i + 1) begin
            memory[i] = 0;
        end
    end

    // Address calculation and memory read
    always @(*) begin
        addr = (((digit << 4) | decade) << 6) | time_slot;
        if (!ce_n && !oe_n && we_n) begin
            data_out = memory[addr];
        end
    end

    // Tri-state buffer
    assign data = (!ce_n && !oe_n && we_n) ? data_out : 1'bz;

endmodule