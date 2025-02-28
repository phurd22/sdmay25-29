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
// CREATED		"Thu Feb 20 23:15:25 2025"

module timing_module(
	CLK,
	RST,
	control,
	data,
	sign_bit,
	write,
	counter_addr,
	keyboard_addr
);


input wire	CLK;
input wire	RST;
output wire	control;
output wire	data;
output wire	sign_bit;
output wire	write;
output wire	[7:0] counter_addr;
output wire	[7:0] keyboard_addr;

wire	[7:0] counter_addr_ALTERA_SYNTHESIZED;
wire	[7:0] keyboard_addr_ALTERA_SYNTHESIZED;
reg	SYNTHESIZED_WIRE_26;
wire	SYNTHESIZED_WIRE_0;
wire	SYNTHESIZED_WIRE_27;
wire	SYNTHESIZED_WIRE_2;
wire	SYNTHESIZED_WIRE_28;
wire	SYNTHESIZED_WIRE_8;
wire	SYNTHESIZED_WIRE_9;
wire	SYNTHESIZED_WIRE_10;
wire	SYNTHESIZED_WIRE_11;
wire	SYNTHESIZED_WIRE_12;
wire	SYNTHESIZED_WIRE_13;
wire	SYNTHESIZED_WIRE_14;
wire	SYNTHESIZED_WIRE_15;
wire	SYNTHESIZED_WIRE_16;
wire	SYNTHESIZED_WIRE_17;
wire	SYNTHESIZED_WIRE_18;
wire	SYNTHESIZED_WIRE_19;
wire	SYNTHESIZED_WIRE_20;
wire	SYNTHESIZED_WIRE_24;

assign	control = SYNTHESIZED_WIRE_18;
assign	write = SYNTHESIZED_WIRE_2;
assign	SYNTHESIZED_WIRE_28 = 0;
assign	SYNTHESIZED_WIRE_9 = 1;
assign	SYNTHESIZED_WIRE_13 = 1;
assign	SYNTHESIZED_WIRE_14 = 1;
assign	SYNTHESIZED_WIRE_24 = 1;




counter	b2v_inst(
	.CLK(SYNTHESIZED_WIRE_26),
	.RST(SYNTHESIZED_WIRE_0),
	.count(counter_addr_ALTERA_SYNTHESIZED));


assign	SYNTHESIZED_WIRE_2 =  ~SYNTHESIZED_WIRE_26;


always@(posedge CLK or negedge SYNTHESIZED_WIRE_27 or negedge SYNTHESIZED_WIRE_27)
begin
if (!SYNTHESIZED_WIRE_27)
	begin
	SYNTHESIZED_WIRE_26 <= 0;
	end
else
if (!SYNTHESIZED_WIRE_27)
	begin
	SYNTHESIZED_WIRE_26 <= 1;
	end
else
	begin
	SYNTHESIZED_WIRE_26 <= SYNTHESIZED_WIRE_2;
	end
end

assign	SYNTHESIZED_WIRE_27 =  ~RST;


adder_4bit	b2v_inst13(
	.X3(counter_addr_ALTERA_SYNTHESIZED[7]),
	.Y3(SYNTHESIZED_WIRE_28),
	.X2(counter_addr_ALTERA_SYNTHESIZED[6]),
	.Y2(SYNTHESIZED_WIRE_28),
	.X1(counter_addr_ALTERA_SYNTHESIZED[5]),
	.Y1(SYNTHESIZED_WIRE_28),
	.X0(counter_addr_ALTERA_SYNTHESIZED[4]),
	.Y0(SYNTHESIZED_WIRE_28),
	.Ci(SYNTHESIZED_WIRE_8),
	.S0(keyboard_addr_ALTERA_SYNTHESIZED[4]),
	.S1(keyboard_addr_ALTERA_SYNTHESIZED[5]),
	.S2(keyboard_addr_ALTERA_SYNTHESIZED[6]),
	.S3(keyboard_addr_ALTERA_SYNTHESIZED[7])
	);

assign	SYNTHESIZED_WIRE_11 = counter_addr_ALTERA_SYNTHESIZED[5] & counter_addr_ALTERA_SYNTHESIZED[4] & counter_addr_ALTERA_SYNTHESIZED[3] & counter_addr_ALTERA_SYNTHESIZED[2];

assign	SYNTHESIZED_WIRE_18 = SYNTHESIZED_WIRE_9 & SYNTHESIZED_WIRE_10 & counter_addr_ALTERA_SYNTHESIZED[5] & counter_addr_ALTERA_SYNTHESIZED[4];

assign	SYNTHESIZED_WIRE_0 = SYNTHESIZED_WIRE_11 | RST;

assign	sign_bit = counter_addr_ALTERA_SYNTHESIZED[4] & counter_addr_ALTERA_SYNTHESIZED[5] & SYNTHESIZED_WIRE_12 & SYNTHESIZED_WIRE_13;

assign	SYNTHESIZED_WIRE_12 = SYNTHESIZED_WIRE_14 & SYNTHESIZED_WIRE_15 & SYNTHESIZED_WIRE_16 & SYNTHESIZED_WIRE_17;


assign	SYNTHESIZED_WIRE_20 = counter_addr_ALTERA_SYNTHESIZED[2] | counter_addr_ALTERA_SYNTHESIZED[3];



assign	SYNTHESIZED_WIRE_15 = counter_addr_ALTERA_SYNTHESIZED[0] ^ counter_addr_ALTERA_SYNTHESIZED[1];

assign	SYNTHESIZED_WIRE_19 = counter_addr_ALTERA_SYNTHESIZED[1] & counter_addr_ALTERA_SYNTHESIZED[0];

assign	data =  ~SYNTHESIZED_WIRE_18;

assign	SYNTHESIZED_WIRE_10 = SYNTHESIZED_WIRE_19 | SYNTHESIZED_WIRE_20;


adder_4bit	b2v_inst5(
	.X3(counter_addr_ALTERA_SYNTHESIZED[3]),
	.Y3(SYNTHESIZED_WIRE_28),
	.X2(counter_addr_ALTERA_SYNTHESIZED[2]),
	.Y2(SYNTHESIZED_WIRE_28),
	.X1(counter_addr_ALTERA_SYNTHESIZED[1]),
	.Y1(SYNTHESIZED_WIRE_28),
	.X0(counter_addr_ALTERA_SYNTHESIZED[0]),
	.Y0(SYNTHESIZED_WIRE_24),
	.Ci(SYNTHESIZED_WIRE_28),
	.S0(keyboard_addr_ALTERA_SYNTHESIZED[0]),
	.S1(keyboard_addr_ALTERA_SYNTHESIZED[1]),
	.S2(keyboard_addr_ALTERA_SYNTHESIZED[2]),
	.S3(keyboard_addr_ALTERA_SYNTHESIZED[3]),
	.Co(SYNTHESIZED_WIRE_8));


assign	SYNTHESIZED_WIRE_16 =  ~counter_addr_ALTERA_SYNTHESIZED[2];

assign	SYNTHESIZED_WIRE_17 =  ~counter_addr_ALTERA_SYNTHESIZED[3];

assign	counter_addr = counter_addr_ALTERA_SYNTHESIZED;
assign	keyboard_addr = keyboard_addr_ALTERA_SYNTHESIZED;

endmodule
