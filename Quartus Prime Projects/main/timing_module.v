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
// CREATED		"Thu Mar 13 23:41:30 2025"

module timing_module(
	rst_n,
	clk,
	en,
	counter_rst_n,
	lastRead,
	lastWrite,
	checkInputs,
	clearCarry,
	write,
	read,
	counter_addr
);


input wire	rst_n;
input wire	clk;
input wire	en;
input wire	counter_rst_n;
output wire	lastRead;
output wire	lastWrite;
output wire	checkInputs;
output wire	clearCarry;
output wire	write;
output wire	read;
output wire	[5:0] counter_addr;

wire	[5:0] counter_addr_ALTERA_SYNTHESIZED;
wire	[3:0] preset;
reg	slow_clk;
wire	SYNTHESIZED_WIRE_10;
wire	SYNTHESIZED_WIRE_11;
wire	SYNTHESIZED_WIRE_3;
wire	SYNTHESIZED_WIRE_12;
wire	SYNTHESIZED_WIRE_6;
wire	SYNTHESIZED_WIRE_7;

assign	SYNTHESIZED_WIRE_10 = 1;
assign	SYNTHESIZED_WIRE_11 = 0;
assign	SYNTHESIZED_WIRE_3 = 1;




always@(posedge clk or negedge rst_n or negedge rst_n)
begin
if (!rst_n)
	begin
	slow_clk <= 0;
	end
else
if (!rst_n)
	begin
	slow_clk <= 1;
	end
else
	slow_clk <= slow_clk ^ SYNTHESIZED_WIRE_10;
end


timing_memory	b2v_inst1(
	.clk(slow_clk),
	.ce_n(SYNTHESIZED_WIRE_11),
	.oe_n(SYNTHESIZED_WIRE_11),
	.we_n(SYNTHESIZED_WIRE_3),
	.addr(counter_addr_ALTERA_SYNTHESIZED),
	.read(read),
	.write(write),
	.lastRead(lastRead),
	.lastWrite(lastWrite),
	.checkInputs(checkInputs),
	.clearCarry(clearCarry),
	.resetCounter_n(SYNTHESIZED_WIRE_7));






four_bit_counter	b2v_inst6(
	.rst_n(SYNTHESIZED_WIRE_12),
	.clk(slow_clk),
	.spe_n(SYNTHESIZED_WIRE_10),
	.te(SYNTHESIZED_WIRE_6),
	.P(preset),
	.Q0(counter_addr_ALTERA_SYNTHESIZED[4]),
	.Q1(counter_addr_ALTERA_SYNTHESIZED[5])
	
	
	);

assign	SYNTHESIZED_WIRE_12 = counter_rst_n & SYNTHESIZED_WIRE_7;


four_bit_counter	b2v_inst92(
	.rst_n(SYNTHESIZED_WIRE_12),
	.clk(slow_clk),
	.spe_n(SYNTHESIZED_WIRE_10),
	.te(en),
	.P(preset),
	.Q0(counter_addr_ALTERA_SYNTHESIZED[0]),
	.Q1(counter_addr_ALTERA_SYNTHESIZED[1]),
	.Q2(counter_addr_ALTERA_SYNTHESIZED[2]),
	.Q3(counter_addr_ALTERA_SYNTHESIZED[3]),
	.TC(SYNTHESIZED_WIRE_6));

assign	counter_addr = counter_addr_ALTERA_SYNTHESIZED;
assign	preset = 4'b0000;

endmodule
