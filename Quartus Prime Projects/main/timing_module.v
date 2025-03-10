// Copyright (C) 2024  Intel Corporation. All rights reserved.
// Your use of Intel Corporation's design tools, logic functions 
// and other software and tools, and any partner logic 
// functions, and any output files from any of the foregoing 
// (including device programming or simulation files), and any 
// associated documentation or information are expressly subject 
// to the terms and conditions of the Intel Program License 
// Subscription Agreement, the Intel Quartus Prime License Agreement,
// the Intel FPGA IP License Agreement, or other applicable license
// agreement, including, without limitation, that your use is for
// the sole purpose of programming logic devices manufactured by
// Intel and sold by Intel or its authorized distributors.  Please
// refer to the applicable agreement for further details, at
// https://fpgasoftware.intel.com/eula.

// PROGRAM		"Quartus Prime"
// VERSION		"Version 23.1std.1 Build 993 05/14/2024 SC Standard Edition"
// CREATED		"Mon Mar 10 17:16:33 2025"

module timing_module(
	CLK,
	rst_n,
	sign_bit,
	write_n,
	control_n,
	data_n,
	zero_n,
	counter_addr
);


input wire	CLK;
input wire	rst_n;
output wire	sign_bit;
output wire	write_n;
output wire	control_n;
output wire	data_n;
output wire	zero_n;
output wire	[5:0] counter_addr;

wire	[5:0] counter_addr_ALTERA_SYNTHESIZED;
reg	SYNTHESIZED_WIRE_15;
wire	SYNTHESIZED_WIRE_0;
wire	SYNTHESIZED_WIRE_1;
wire	SYNTHESIZED_WIRE_2;
wire	SYNTHESIZED_WIRE_3;
wire	SYNTHESIZED_WIRE_4;
wire	SYNTHESIZED_WIRE_5;
wire	SYNTHESIZED_WIRE_6;
wire	SYNTHESIZED_WIRE_7;
wire	SYNTHESIZED_WIRE_8;
wire	SYNTHESIZED_WIRE_9;
wire	SYNTHESIZED_WIRE_10;
wire	SYNTHESIZED_WIRE_11;
wire	SYNTHESIZED_WIRE_12;
wire	SYNTHESIZED_WIRE_13;
wire	SYNTHESIZED_WIRE_14;

assign	write_n = SYNTHESIZED_WIRE_15;
assign	data_n = SYNTHESIZED_WIRE_10;
assign	SYNTHESIZED_WIRE_2 = 1;
assign	SYNTHESIZED_WIRE_5 = 1;
assign	SYNTHESIZED_WIRE_6 = 1;




counter	b2v_inst(
	.CLK(SYNTHESIZED_WIRE_15),
	.rst_n(SYNTHESIZED_WIRE_0),
	.count(counter_addr_ALTERA_SYNTHESIZED));


assign	SYNTHESIZED_WIRE_1 =  ~SYNTHESIZED_WIRE_15;


always@(posedge CLK or negedge rst_n or negedge rst_n)
begin
if (!rst_n)
	begin
	SYNTHESIZED_WIRE_15 <= 0;
	end
else
if (!rst_n)
	begin
	SYNTHESIZED_WIRE_15 <= 1;
	end
else
	begin
	SYNTHESIZED_WIRE_15 <= SYNTHESIZED_WIRE_1;
	end
end

assign	SYNTHESIZED_WIRE_14 = counter_addr_ALTERA_SYNTHESIZED[5] & counter_addr_ALTERA_SYNTHESIZED[4] & counter_addr_ALTERA_SYNTHESIZED[3] & counter_addr_ALTERA_SYNTHESIZED[2];

assign	SYNTHESIZED_WIRE_10 = SYNTHESIZED_WIRE_2 & SYNTHESIZED_WIRE_3 & counter_addr_ALTERA_SYNTHESIZED[5] & counter_addr_ALTERA_SYNTHESIZED[4];

assign	sign_bit = counter_addr_ALTERA_SYNTHESIZED[4] & counter_addr_ALTERA_SYNTHESIZED[5] & SYNTHESIZED_WIRE_4 & SYNTHESIZED_WIRE_5;

assign	SYNTHESIZED_WIRE_4 = SYNTHESIZED_WIRE_6 & SYNTHESIZED_WIRE_7 & SYNTHESIZED_WIRE_8 & SYNTHESIZED_WIRE_9;

assign	SYNTHESIZED_WIRE_12 = counter_addr_ALTERA_SYNTHESIZED[2] | counter_addr_ALTERA_SYNTHESIZED[3];


assign	SYNTHESIZED_WIRE_7 = counter_addr_ALTERA_SYNTHESIZED[0] ^ counter_addr_ALTERA_SYNTHESIZED[1];

assign	SYNTHESIZED_WIRE_11 = counter_addr_ALTERA_SYNTHESIZED[1] & counter_addr_ALTERA_SYNTHESIZED[0];

assign	control_n =  ~SYNTHESIZED_WIRE_10;

assign	SYNTHESIZED_WIRE_3 = SYNTHESIZED_WIRE_11 | SYNTHESIZED_WIRE_12;

assign	zero_n = counter_addr_ALTERA_SYNTHESIZED[0] | counter_addr_ALTERA_SYNTHESIZED[2] | counter_addr_ALTERA_SYNTHESIZED[1] | counter_addr_ALTERA_SYNTHESIZED[3] | counter_addr_ALTERA_SYNTHESIZED[4] | counter_addr_ALTERA_SYNTHESIZED[5];


assign	SYNTHESIZED_WIRE_8 =  ~counter_addr_ALTERA_SYNTHESIZED[2];

assign	SYNTHESIZED_WIRE_9 =  ~counter_addr_ALTERA_SYNTHESIZED[3];

assign	SYNTHESIZED_WIRE_0 = rst_n & SYNTHESIZED_WIRE_13;

assign	SYNTHESIZED_WIRE_13 =  ~SYNTHESIZED_WIRE_14;

assign	counter_addr = counter_addr_ALTERA_SYNTHESIZED;

endmodule
