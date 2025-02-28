
for i in range (1, 10):
    for j in range (0, 15):
        print(f"Number: {i}\tDecade: {j}")
        num = (i << 4)|j
        for k in range (0, 50):
            nnum = (num << 4) | k
            bit = 0
            if ((i*pow(10,j)) & (1 << k)) == (1 << k):
                bit = 1
            print(f"\tAddress (hex): {hex(nnum)}\tBit: {bit}")
