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
// CREATED		"Thu Feb 20 21:54:45 2025"

module four_bit_counter(
	En,
	CLK,
	CLRN,
	Q
);


input wire	En;
input wire	CLK;
input wire	CLRN;
output wire	[3:0] Q;

reg	[3:0] Q_ALTERA_SYNTHESIZED;
wire	SYNTHESIZED_WIRE_13;
wire	SYNTHESIZED_WIRE_1;
wire	SYNTHESIZED_WIRE_14;
wire	SYNTHESIZED_WIRE_3;
wire	SYNTHESIZED_WIRE_15;
wire	SYNTHESIZED_WIRE_6;
wire	SYNTHESIZED_WIRE_8;
wire	SYNTHESIZED_WIRE_12;

assign	SYNTHESIZED_WIRE_14 = 1;



assign	SYNTHESIZED_WIRE_15 = Q_ALTERA_SYNTHESIZED[1] & SYNTHESIZED_WIRE_13;


always@(posedge CLK or negedge CLRN or negedge SYNTHESIZED_WIRE_14)
begin
if (!CLRN)
	begin
	Q_ALTERA_SYNTHESIZED[0] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_14)
	begin
	Q_ALTERA_SYNTHESIZED[0] <= 1;
	end
else
	begin
	Q_ALTERA_SYNTHESIZED[0] <= SYNTHESIZED_WIRE_1;
	end
end


always@(posedge CLK or negedge CLRN or negedge SYNTHESIZED_WIRE_14)
begin
if (!CLRN)
	begin
	Q_ALTERA_SYNTHESIZED[1] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_14)
	begin
	Q_ALTERA_SYNTHESIZED[1] <= 1;
	end
else
	begin
	Q_ALTERA_SYNTHESIZED[1] <= SYNTHESIZED_WIRE_3;
	end
end

assign	SYNTHESIZED_WIRE_12 = Q_ALTERA_SYNTHESIZED[2] & SYNTHESIZED_WIRE_15;


always@(posedge CLK or negedge CLRN or negedge SYNTHESIZED_WIRE_14)
begin
if (!CLRN)
	begin
	Q_ALTERA_SYNTHESIZED[2] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_14)
	begin
	Q_ALTERA_SYNTHESIZED[2] <= 1;
	end
else
	begin
	Q_ALTERA_SYNTHESIZED[2] <= SYNTHESIZED_WIRE_6;
	end
end


always@(posedge CLK or negedge CLRN or negedge SYNTHESIZED_WIRE_14)
begin
if (!CLRN)
	begin
	Q_ALTERA_SYNTHESIZED[3] <= 0;
	end
else
if (!SYNTHESIZED_WIRE_14)
	begin
	Q_ALTERA_SYNTHESIZED[3] <= 1;
	end
else
	begin
	Q_ALTERA_SYNTHESIZED[3] <= SYNTHESIZED_WIRE_8;
	end
end

assign	SYNTHESIZED_WIRE_1 = En ^ Q_ALTERA_SYNTHESIZED[0];

assign	SYNTHESIZED_WIRE_3 = SYNTHESIZED_WIRE_13 ^ Q_ALTERA_SYNTHESIZED[1];

assign	SYNTHESIZED_WIRE_6 = SYNTHESIZED_WIRE_15 ^ Q_ALTERA_SYNTHESIZED[2];

assign	SYNTHESIZED_WIRE_8 = SYNTHESIZED_WIRE_12 ^ Q_ALTERA_SYNTHESIZED[3];

assign	SYNTHESIZED_WIRE_13 = Q_ALTERA_SYNTHESIZED[0] & En;


assign	Q = Q_ALTERA_SYNTHESIZED;

endmodule
