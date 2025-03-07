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
// CREATED		"Sun Mar  2 23:13:44 2025"

module timing_module(
	CLK,
	RST,
	control,
	data,
	sign_bit,
	write,
	counter_addr
);


input wire	CLK;
input wire	RST;
output wire	control;
output wire	data;
output wire	sign_bit;
output wire	write;
output wire	[5:0] counter_addr;

wire	[5:0] counter_addr_ALTERA_SYNTHESIZED;
reg	SYNTHESIZED_WIRE_16;
wire	SYNTHESIZED_WIRE_0;
wire	SYNTHESIZED_WIRE_17;
wire	SYNTHESIZED_WIRE_2;
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
wire	SYNTHESIZED_WIRE_15;

assign	control = SYNTHESIZED_WIRE_13;
assign	write = SYNTHESIZED_WIRE_2;
assign	SYNTHESIZED_WIRE_4 = 1;
assign	SYNTHESIZED_WIRE_8 = 1;
assign	SYNTHESIZED_WIRE_9 = 1;




counter	b2v_inst(
	.CLK(SYNTHESIZED_WIRE_16),
	.RST(SYNTHESIZED_WIRE_0),
	.count(counter_addr_ALTERA_SYNTHESIZED));


assign	SYNTHESIZED_WIRE_2 =  ~SYNTHESIZED_WIRE_16;


always@(posedge CLK or negedge SYNTHESIZED_WIRE_17 or negedge SYNTHESIZED_WIRE_17)
begin
if (!SYNTHESIZED_WIRE_17)
	begin
	SYNTHESIZED_WIRE_16 <= 0;
	end
else
if (!SYNTHESIZED_WIRE_17)
	begin
	SYNTHESIZED_WIRE_16 <= 1;
	end
else
	begin
	SYNTHESIZED_WIRE_16 <= SYNTHESIZED_WIRE_2;
	end
end

assign	SYNTHESIZED_WIRE_17 =  ~RST;

assign	SYNTHESIZED_WIRE_6 = counter_addr_ALTERA_SYNTHESIZED[5] & counter_addr_ALTERA_SYNTHESIZED[4] & counter_addr_ALTERA_SYNTHESIZED[3] & counter_addr_ALTERA_SYNTHESIZED[2];

assign	SYNTHESIZED_WIRE_13 = SYNTHESIZED_WIRE_4 & SYNTHESIZED_WIRE_5 & counter_addr_ALTERA_SYNTHESIZED[5] & counter_addr_ALTERA_SYNTHESIZED[4];

assign	SYNTHESIZED_WIRE_0 = SYNTHESIZED_WIRE_6 | RST;

assign	sign_bit = counter_addr_ALTERA_SYNTHESIZED[4] & counter_addr_ALTERA_SYNTHESIZED[5] & SYNTHESIZED_WIRE_7 & SYNTHESIZED_WIRE_8;

assign	SYNTHESIZED_WIRE_7 = SYNTHESIZED_WIRE_9 & SYNTHESIZED_WIRE_10 & SYNTHESIZED_WIRE_11 & SYNTHESIZED_WIRE_12;

assign	SYNTHESIZED_WIRE_15 = counter_addr_ALTERA_SYNTHESIZED[2] | counter_addr_ALTERA_SYNTHESIZED[3];


assign	SYNTHESIZED_WIRE_10 = counter_addr_ALTERA_SYNTHESIZED[0] ^ counter_addr_ALTERA_SYNTHESIZED[1];

assign	SYNTHESIZED_WIRE_14 = counter_addr_ALTERA_SYNTHESIZED[1] & counter_addr_ALTERA_SYNTHESIZED[0];

assign	data =  ~SYNTHESIZED_WIRE_13;

assign	SYNTHESIZED_WIRE_5 = SYNTHESIZED_WIRE_14 | SYNTHESIZED_WIRE_15;


assign	SYNTHESIZED_WIRE_11 =  ~counter_addr_ALTERA_SYNTHESIZED[2];

assign	SYNTHESIZED_WIRE_12 =  ~counter_addr_ALTERA_SYNTHESIZED[3];

assign	counter_addr = counter_addr_ALTERA_SYNTHESIZED;

endmodule
