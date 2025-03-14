module eeprom(
	input wire [13:0] addr,
	output wire data,
	input wire ce_n,
	input wire oe_n,
	input wire we_n
);

	reg memory [0:16383] // 2^14 = 16384 locations
	
	integer i, j, k;
	initial begin
		for (i = 0; i < 4'h4000; i = i + 1) begin
			for (j = 0; j < 15; j = j + 1) begin
				for (k = 0; k < 50; k = k + 1) begin
					integer num = (((i << 4) | j) << 6) | k;
					integer bit = 0;
					
					if ((i*pow(10, j)) & (1 << k)) == (1 << k) begin
						bit = 1;
					end
					
					memory[num] = bit;
				end
			end
		end
	end
	
	