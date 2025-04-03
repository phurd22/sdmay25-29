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
// CREATED		"Tue Apr  1 18:02:09 2025"

module input_reg(
	clk,
	checkInputs,
	ReadBase10_button,
	clearInputs,
	rst_n,
	ReadBase10
);


input wire	clk;
input wire	checkInputs;
input wire	ReadBase10_button;
input wire	clearInputs;
input wire	rst_n;
output wire	ReadBase10;

wire	check;
wire	clear;
wire	SYNTHESIZED_WIRE_0;
wire	SYNTHESIZED_WIRE_1;
wire	SYNTHESIZED_WIRE_8;
wire	SYNTHESIZED_WIRE_3;
reg	SYNTHESIZED_WIRE_9;
reg	DFF_inst1;
wire	SYNTHESIZED_WIRE_5;
wire	SYNTHESIZED_WIRE_6;
wire	SYNTHESIZED_WIRE_7;

assign	ReadBase10 = DFF_inst1;
assign	SYNTHESIZED_WIRE_8 = 1;




always@(posedge SYNTHESIZED_WIRE_1 or negedge SYNTHESIZED_WIRE_0 or negedge SYNTHESIZED_WIRE_8)
begin
if (!SYNTHESIZED_WIRE_0)
	begin
	SYNTHESIZED_WIRE_9 <= 0;
	end
else
if (!SYNTHESIZED_WIRE_8)
	begin
	SYNTHESIZED_WIRE_9 <= 1;
	end
else
	begin
	SYNTHESIZED_WIRE_9 <= ReadBase10_button;
	end
end


always@(posedge check or negedge SYNTHESIZED_WIRE_3 or negedge SYNTHESIZED_WIRE_8)
begin
if (!SYNTHESIZED_WIRE_3)
	begin
	DFF_inst1 <= 0;
	end
else
if (!SYNTHESIZED_WIRE_8)
	begin
	DFF_inst1 <= 1;
	end
else
	begin
	DFF_inst1 <= SYNTHESIZED_WIRE_9;
	end
end

assign	SYNTHESIZED_WIRE_5 =  ~SYNTHESIZED_WIRE_9;

assign	SYNTHESIZED_WIRE_7 =  ~DFF_inst1;

assign	SYNTHESIZED_WIRE_6 =  ~clear;

assign	SYNTHESIZED_WIRE_1 = clk & SYNTHESIZED_WIRE_5;

assign	SYNTHESIZED_WIRE_3 = rst_n & SYNTHESIZED_WIRE_6;

assign	SYNTHESIZED_WIRE_0 = rst_n & SYNTHESIZED_WIRE_7;


assign	clear = clearInputs;
assign	check = checkInputs;

endmodule
