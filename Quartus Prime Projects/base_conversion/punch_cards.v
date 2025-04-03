module punch_cards(

	input wire [1:0] card_slt,
	input wire mask,
	input wire [3:0] card_addr,
	
	output wire [15:0] card_out

);


    reg [19:0] eq1 [0:14];

	 // 2x + 5y + 3z = 5
	 initial begin
    eq1[0] = 16'h2535F;
	 eq1[1] = 16'hFFFFF;
	 eq1[2] = 16'hFFFFF;
	 eq1[3] = 16'hFFFFF;
	 eq1[4] = 16'hFFFFF;
	 
	 eq1[5] = 16'hFFFFF;
	 eq1[6] = 16'hFFFFF;
	 eq1[7] = 16'hFFFFF;
	 eq1[8] = 16'hFFFFF;
	 eq1[9] = 16'hFFFFF;
	 
	 eq1[10] = 16'hFFFFF;
	 eq1[11] = 16'hFFFFF;
	 eq1[12] = 16'hFFFFF;
	 eq1[13] = 16'hFFFFF;
	 eq1[14] = 16'hFFFFF;
	 end
	 
	 reg [19:0] eq2 [0:14];

	 // x + 2y + 3z = 4
	 initial begin
    eq2[0] = 16'h1234F;
	 eq2[1] = 16'hFFFFF;
	 eq2[2] = 16'hFFFFF;
	 eq2[3] = 16'hFFFFF;
	 eq2[4] = 16'hFFFFF;
	 
	 eq2[5] = 16'hFFFFF;
	 eq2[6] = 16'hFFFFF;
	 eq2[7] = 16'hFFFFF;
	 eq2[8] = 16'hFFFFF;
	 eq2[9] = 16'hFFFFF;
	 
	 eq2[10] = 16'hFFFFF;
	 eq2[11] = 16'hFFFFF;
	 eq2[12] = 16'hFFFFF;
	 eq2[13] = 16'hFFFFF;
	 eq2[14] = 16'hFFFFF;
	 end
	 
	 reg [19:0] eq3 [0:14];

	 // x + 0y + 8z = 9
	 initial begin
    eq3[0] = 16'h1089F;
	 eq3[1] = 16'hFFFFF;
	 eq3[2] = 16'hFFFFF;
	 eq3[3] = 16'hFFFFF;
	 eq3[4] = 16'hFFFFF;
	 
	 eq3[5] = 16'hFFFFF;
	 eq3[6] = 16'hFFFFF;
	 eq3[7] = 16'hFFFFF;
	 eq3[8] = 16'hFFFFF;
	 eq3[9] = 16'hFFFFF;
	 
	 eq3[10] = 16'hFFFFF;
	 eq3[11] = 16'hFFFFF;
	 eq3[12] = 16'hFFFFF;
	 eq3[13] = 16'hFFFFF;
	 eq3[14] = 16'hFFFFF;
	 end
	 
	 reg [19:0] maskX [0:14];

	 // powers of 10 for X
	 initial begin
    maskX[0] = 16'h1FFFF;
	 maskX[1] = 16'h1FFFF;
	 maskX[2] = 16'h1FFFF;
	 maskX[3] = 16'h1FFFF;
	 maskX[4] = 16'h1FFFF;
	 
	 maskX[5] = 16'h1FFFF;
	 maskX[6] = 16'h1FFFF;
	 maskX[7] = 16'h1FFFF;
	 maskX[8] = 16'h1FFFF;
	 maskX[9] = 16'h1FFFF;
	 
	 maskX[10] = 16'h1FFFF;
	 maskX[11] = 16'h1FFFF;
	 maskX[12] = 16'h1FFFF;
	 maskX[13] = 16'h1FFFF;
	 maskX[14] = 16'h1FFFF;
	 end
	 
	 reg [19:0] maskY [0:14];

	 // powers of 10 for Y
	 initial begin
    maskY[0] = 16'hF1FFF;
	 maskY[1] = 16'hF1FFF;
	 maskY[2] = 16'hF1FFF;
	 maskY[3] = 16'hF1FFF;
	 maskY[4] = 16'hF1FFF;
	 
	 maskY[5] = 16'hF1FFF;
	 maskY[6] = 16'hF1FFF;
	 maskY[7] = 16'hF1FFF;
	 maskY[8] = 16'hF1FFF;
	 maskY[9] = 16'hF1FFF;
	 
	 maskY[10] = 16'hF1FFF;
	 maskY[11] = 16'hF1FFF;
	 maskY[12] = 16'hF1FFF;
	 maskY[13] = 16'hF1FFF;
	 maskY[14] = 16'hF1FFF;
	 end
	 
	 reg [19:0] maskZ [0:14];

	 // powers of 10 for Z
	 initial begin
    maskZ[0] = 16'hFF1FF;
	 maskZ[1] = 16'hFF1FF;
	 maskZ[2] = 16'hFF1FF;
	 maskZ[3] = 16'hFF1FF;
	 maskZ[4] = 16'hFF1FF;
	 
	 maskZ[5] = 16'hFF1FF;
	 maskZ[6] = 16'hFF1FF;
	 maskZ[7] = 16'hFF1FF;
	 maskZ[8] = 16'hFF1FF;
	 maskZ[9] = 16'hFF1FF;
	 
	 maskZ[10] = 16'hFF1FF;
	 maskZ[11] = 16'hFF1FF;
	 maskZ[12] = 16'hFF1FF;
	 maskZ[13] = 16'hFF1FF;
	 maskZ[14] = 16'hFF1FF;
	 end
	 
	 reg [19:0] maskB [0:14];

	 // powers of 10 for B
	 initial begin
    maskB[0] = 16'hFFF1F;
	 maskB[1] = 16'hFFF1F;
	 maskB[2] = 16'hFFF1F;
	 maskB[3] = 16'hFFF1F;
	 maskB[4] = 16'hFFF1F;
	 
	 maskB[5] = 16'hFFF1F;
	 maskB[6] = 16'hFFF1F;
	 maskB[7] = 16'hFFF1F;
	 maskB[8] = 16'hFFF1F;
	 maskB[9] = 16'hFFF1F;
	 
	 maskB[10] = 16'hFFF1F;
	 maskB[11] = 16'hFFF1F;
	 maskB[12] = 16'hFFF1F;
	 maskB[13] = 16'hFFF1F;
	 maskB[14] = 16'hFFF1F;
	 end
	 
	 reg [15:0] card;
	 
    // Read operation
    always @(*) begin
        if (mask) begin
            // Read data from memory when output enable is active
            card = (card_slt[1]) ? ((card_slt[0]) ? (maskB[card_addr]):(maskZ[card_addr])):((card_slt[0]) ? (maskY[card_addr]):(maskX[card_addr]));
        end else begin
            card = (card_slt[1]) ? ((card_slt[0]) ? (eq3[card_addr]):(eq2[card_addr])):((card_slt[0]) ? (eq1[card_addr]):(16'bZ));
        end
    end
	 
	 assign card_out = card;
	 
endmodule