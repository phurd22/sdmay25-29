module punch_cards(

	input wire [1:0] card_slt,
	input wire mask,
	input wire [3:0] card_addr,
	
	output wire [19:0] card_out

);


    reg [19:0] eq1 [0:14];

	 // 2x + 5y + 3z = 5
	 initial begin
    eq1[0] = 20'h2535F;
	 eq1[1] = 20'hFFFFF;
	 eq1[2] = 20'hFFFFF;
	 eq1[3] = 20'hFFFFF;
	 eq1[4] = 20'hFFFFF;
	 
	 eq1[5] = 20'hFFFFF;
	 eq1[6] = 20'hFFFFF;
	 eq1[7] = 20'hFFFFF;
	 eq1[8] = 20'hFFFFF;
	 eq1[9] = 20'hFFFFF;
	 
	 eq1[10] = 20'hFFFFF;
	 eq1[11] = 20'hFFFFF;
	 eq1[12] = 20'hFFFFF;
	 eq1[13] = 20'hFFFFF;
	 eq1[14] = 20'hFFFFF;
	 end
	 
	 reg [19:0] eq2 [0:14];

	 // x + 2y + 3z = 4
	 initial begin
    eq2[0] = 20'h1234F;
	 eq2[1] = 20'hFFFFF;
	 eq2[2] = 20'hFFFFF;
	 eq2[3] = 20'hFFFFF;
	 eq2[4] = 20'hFFFFF;
	 
	 eq2[5] = 20'hFFFFF;
	 eq2[6] = 20'hFFFFF;
	 eq2[7] = 20'hFFFFF;
	 eq2[8] = 20'hFFFFF;
	 eq2[9] = 20'hFFFFF;
	 
	 eq2[10] = 20'hFFFFF;
	 eq2[11] = 20'hFFFFF;
	 eq2[12] = 20'hFFFFF;
	 eq2[13] = 20'hFFFFF;
	 eq2[14] = 20'hFFFFF;
	 end
	 
	 reg [19:0] eq3 [0:14];

	 // x + 0y + 8z = 9
	 initial begin
    eq3[0] = 20'h1089F;
	 eq3[1] = 20'hFFFFF;
	 eq3[2] = 20'hFFFFF;
	 eq3[3] = 20'hFFFFF;
	 eq3[4] = 20'hFFFFF;
	 
	 eq3[5] = 20'hFFFFF;
	 eq3[6] = 20'hFFFFF;
	 eq3[7] = 20'hFFFFF;
	 eq3[8] = 20'hFFFFF;
	 eq3[9] = 20'hFFFFF;
	 
	 eq3[10] = 20'hFFFFF;
	 eq3[11] = 20'hFFFFF;
	 eq3[12] = 20'hFFFFF;
	 eq3[13] = 20'hFFFFF;
	 eq3[14] = 20'hFFFFF;
	 end
	 
	 reg [19:0] maskX [0:14];

	 // powers of 10 for X
	 initial begin
    maskX[0] = 20'h1FFFF;
	 maskX[1] = 20'h1FFFF;
	 maskX[2] = 20'h1FFFF;
	 maskX[3] = 20'h1FFFF;
	 maskX[4] = 20'h1FFFF;
	 
	 maskX[5] = 20'h1FFFF;
	 maskX[6] = 20'h1FFFF;
	 maskX[7] = 20'h1FFFF;
	 maskX[8] = 20'h1FFFF;
	 maskX[9] = 20'h1FFFF;
	 
	 maskX[10] = 20'h1FFFF;
	 maskX[11] = 20'h1FFFF;
	 maskX[12] = 20'h1FFFF;
	 maskX[13] = 20'h1FFFF;
	 maskX[14] = 20'h1FFFF;
	 end
	 
	 reg [19:0] maskY [0:14];

	 // powers of 10 for Y
	 initial begin
    maskY[0] = 20'hF1FFF;
	 maskY[1] = 20'hF1FFF;
	 maskY[2] = 20'hF1FFF;
	 maskY[3] = 20'hF1FFF;
	 maskY[4] = 20'hF1FFF;
	 
	 maskY[5] = 20'hF1FFF;
	 maskY[6] = 20'hF1FFF;
	 maskY[7] = 20'hF1FFF;
	 maskY[8] = 20'hF1FFF;
	 maskY[9] = 20'hF1FFF;
	 
	 maskY[10] = 20'hF1FFF;
	 maskY[11] = 20'hF1FFF;
	 maskY[12] = 20'hF1FFF;
	 maskY[13] = 20'hF1FFF;
	 maskY[14] = 20'hF1FFF;
	 end
	 
	 reg [19:0] maskZ [0:14];

	 // powers of 10 for Z
	 initial begin
    maskZ[0] = 20'hFF1FF;
	 maskZ[1] = 20'hFF1FF;
	 maskZ[2] = 20'hFF1FF;
	 maskZ[3] = 20'hFF1FF;
	 maskZ[4] = 20'hFF1FF;
	 
	 maskZ[5] = 20'hFF1FF;
	 maskZ[6] = 20'hFF1FF;
	 maskZ[7] = 20'hFF1FF;
	 maskZ[8] = 20'hFF1FF;
	 maskZ[9] = 20'hFF1FF;
	 
	 maskZ[10] = 20'hFF1FF;
	 maskZ[11] = 20'hFF1FF;
	 maskZ[12] = 20'hFF1FF;
	 maskZ[13] = 20'hFF1FF;
	 maskZ[14] = 20'hFF1FF;
	 end
	 
	 reg [19:0] maskB [0:14];

	 // powers of 10 for B
	 initial begin
    maskB[0] = 20'hFFF1F;
	 maskB[1] = 20'hFFF1F;
	 maskB[2] = 20'hFFF1F;
	 maskB[3] = 20'hFFF1F;
	 maskB[4] = 20'hFFF1F;
	 
	 maskB[5] = 20'hFFF1F;
	 maskB[6] = 20'hFFF1F;
	 maskB[7] = 20'hFFF1F;
	 maskB[8] = 20'hFFF1F;
	 maskB[9] = 20'hFFF1F;
	 
	 maskB[10] = 20'hFFF1F;
	 maskB[11] = 20'hFFF1F;
	 maskB[12] = 20'hFFF1F;
	 maskB[13] = 20'hFFF1F;
	 maskB[14] = 20'hFFF1F;
	 end
	 
	 reg [19:0] card;
	 
    // Read operation
    always @(*) begin
        if (mask) begin
            // Read data from memory when output enable is active
            card = (card_slt[1]) ? ((card_slt[0]) ? (maskB[card_addr]):(maskZ[card_addr])):((card_slt[0]) ? (maskY[card_addr]):(maskX[card_addr]));
        end else begin
            card = (card_slt[1]) ? ((card_slt[0]) ? (eq3[card_addr]):(eq2[card_addr])):((card_slt[0]) ? (eq1[card_addr]):(20'bZ));
        end
    end
	 
	 assign card_out = card;
	 
endmodule