#digit 4b | decade 4b | ts 6b
import struct
f = open("eeprom_base_conversion.bin", "w")
a = ""
for i in range (0,10):
    for j in range (0, 16):
        for k in range (0,64):
            bit = 0
            if ((i*pow(10,j)) & (1 << k)) == (1 << k):
                bit = 1
            a = a + str(bit)
            if (len(a) == 8):
                f.write(a)
                a = ""

f.close