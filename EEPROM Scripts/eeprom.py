memory = [1 for i in range(0, 0x4000)]
for i in range (1, 10):
    for j in range (0, 15):
        print(f"Number: {i}\tDecade: {j}")
        num = (i << 4)|j
        for k in range (0, 50):
            nnum = (num << 6) | k
            bit = 0
            if ((i*pow(10,j)) & (1 << k)) == (1 << k):
                bit = 1
            memory[nnum] = bit
            print(f"\tAddress (hex): {hex(nnum)}\tBit: {bit}\tMemory Value: {memory[nnum]}")

for i in range(0x3C00, 0x4000):
    print(memory[i])
