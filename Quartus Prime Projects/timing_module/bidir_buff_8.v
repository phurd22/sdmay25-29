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
// CREATED		"Fri Feb 21 11:11:08 2025"

module bidir_buff_8(
	DIR,
	A,
	B
);


input wire	DIR;
inout wire	[7:0] A;
inout wire	[7:0] B;






alt_iobuf	b2v_inst(
	.i(A[0]),
	.oe(DIR),
	.io(B[0]),
	.o(A[0])
	);


alt_iobuf	b2v_inst2(
	.i(A[1]),
	.oe(DIR),
	.io(B[1]),
	.o(A[1])
	);


alt_iobuf	b2v_inst3(
	.i(A[2]),
	.oe(DIR),
	.io(B[2]),
	.o(A[2])
	);


alt_iobuf	b2v_inst4(
	.i(A[3]),
	
	.io(B[3]),
	.o(A[3])
	);


alt_iobuf	b2v_inst5(
	.i(A[4]),
	.oe(DIR),
	.io(B[4]),
	.o(A[4])
	);


alt_iobuf	b2v_inst6(
	.i(A[5]),
	.oe(DIR),
	.io(B[5]),
	.o(A[5])
	);


alt_iobuf	b2v_inst7(
	.i(A[6]),
	.oe(DIR),
	.io(B[6]),
	.o(A[6])
	);


alt_iobuf	b2v_inst8(
	.i(A[7]),
	.oe(DIR),
	.io(B[7]),
	.o(A[7])
	);


endmodule
